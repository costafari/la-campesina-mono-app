jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AbonosService } from '../service/abonos.service';
import { IAbonos, Abonos } from '../abonos.model';
import { IFacturas } from 'app/entities/facturas/facturas.model';
import { FacturasService } from 'app/entities/facturas/service/facturas.service';

import { AbonosUpdateComponent } from './abonos-update.component';

describe('Component Tests', () => {
  describe('Abonos Management Update Component', () => {
    let comp: AbonosUpdateComponent;
    let fixture: ComponentFixture<AbonosUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let abonosService: AbonosService;
    let facturasService: FacturasService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AbonosUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AbonosUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AbonosUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      abonosService = TestBed.inject(AbonosService);
      facturasService = TestBed.inject(FacturasService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Facturas query and add missing value', () => {
        const abonos: IAbonos = { id: 456 };
        const facturas: IFacturas = { id: 26504 };
        abonos.facturas = facturas;

        const facturasCollection: IFacturas[] = [{ id: 32306 }];
        jest.spyOn(facturasService, 'query').mockReturnValue(of(new HttpResponse({ body: facturasCollection })));
        const additionalFacturas = [facturas];
        const expectedCollection: IFacturas[] = [...additionalFacturas, ...facturasCollection];
        jest.spyOn(facturasService, 'addFacturasToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ abonos });
        comp.ngOnInit();

        expect(facturasService.query).toHaveBeenCalled();
        expect(facturasService.addFacturasToCollectionIfMissing).toHaveBeenCalledWith(facturasCollection, ...additionalFacturas);
        expect(comp.facturasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const abonos: IAbonos = { id: 456 };
        const facturas: IFacturas = { id: 71743 };
        abonos.facturas = facturas;

        activatedRoute.data = of({ abonos });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(abonos));
        expect(comp.facturasSharedCollection).toContain(facturas);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Abonos>>();
        const abonos = { id: 123 };
        jest.spyOn(abonosService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ abonos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: abonos }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(abonosService.update).toHaveBeenCalledWith(abonos);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Abonos>>();
        const abonos = new Abonos();
        jest.spyOn(abonosService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ abonos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: abonos }));
        saveSubject.complete();

        // THEN
        expect(abonosService.create).toHaveBeenCalledWith(abonos);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Abonos>>();
        const abonos = { id: 123 };
        jest.spyOn(abonosService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ abonos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(abonosService.update).toHaveBeenCalledWith(abonos);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackFacturasById', () => {
        it('Should return tracked Facturas primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFacturasById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
