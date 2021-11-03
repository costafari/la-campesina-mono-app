jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DetallesService } from '../service/detalles.service';
import { IDetalles, Detalles } from '../detalles.model';
import { IFacturas } from 'app/entities/facturas/facturas.model';
import { FacturasService } from 'app/entities/facturas/service/facturas.service';
import { ILotes } from 'app/entities/lotes/lotes.model';
import { LotesService } from 'app/entities/lotes/service/lotes.service';

import { DetallesUpdateComponent } from './detalles-update.component';

describe('Component Tests', () => {
  describe('Detalles Management Update Component', () => {
    let comp: DetallesUpdateComponent;
    let fixture: ComponentFixture<DetallesUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let detallesService: DetallesService;
    let facturasService: FacturasService;
    let lotesService: LotesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DetallesUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DetallesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DetallesUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      detallesService = TestBed.inject(DetallesService);
      facturasService = TestBed.inject(FacturasService);
      lotesService = TestBed.inject(LotesService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Facturas query and add missing value', () => {
        const detalles: IDetalles = { id: 456 };
        const facturas: IFacturas = { id: 64114 };
        detalles.facturas = facturas;

        const facturasCollection: IFacturas[] = [{ id: 91324 }];
        jest.spyOn(facturasService, 'query').mockReturnValue(of(new HttpResponse({ body: facturasCollection })));
        const additionalFacturas = [facturas];
        const expectedCollection: IFacturas[] = [...additionalFacturas, ...facturasCollection];
        jest.spyOn(facturasService, 'addFacturasToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ detalles });
        comp.ngOnInit();

        expect(facturasService.query).toHaveBeenCalled();
        expect(facturasService.addFacturasToCollectionIfMissing).toHaveBeenCalledWith(facturasCollection, ...additionalFacturas);
        expect(comp.facturasSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Lotes query and add missing value', () => {
        const detalles: IDetalles = { id: 456 };
        const lotes: ILotes = { id: 57374 };
        detalles.lotes = lotes;

        const lotesCollection: ILotes[] = [{ id: 19592 }];
        jest.spyOn(lotesService, 'query').mockReturnValue(of(new HttpResponse({ body: lotesCollection })));
        const additionalLotes = [lotes];
        const expectedCollection: ILotes[] = [...additionalLotes, ...lotesCollection];
        jest.spyOn(lotesService, 'addLotesToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ detalles });
        comp.ngOnInit();

        expect(lotesService.query).toHaveBeenCalled();
        expect(lotesService.addLotesToCollectionIfMissing).toHaveBeenCalledWith(lotesCollection, ...additionalLotes);
        expect(comp.lotesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const detalles: IDetalles = { id: 456 };
        const facturas: IFacturas = { id: 39710 };
        detalles.facturas = facturas;
        const lotes: ILotes = { id: 54033 };
        detalles.lotes = lotes;

        activatedRoute.data = of({ detalles });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(detalles));
        expect(comp.facturasSharedCollection).toContain(facturas);
        expect(comp.lotesSharedCollection).toContain(lotes);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Detalles>>();
        const detalles = { id: 123 };
        jest.spyOn(detallesService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ detalles });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: detalles }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(detallesService.update).toHaveBeenCalledWith(detalles);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Detalles>>();
        const detalles = new Detalles();
        jest.spyOn(detallesService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ detalles });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: detalles }));
        saveSubject.complete();

        // THEN
        expect(detallesService.create).toHaveBeenCalledWith(detalles);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Detalles>>();
        const detalles = { id: 123 };
        jest.spyOn(detallesService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ detalles });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(detallesService.update).toHaveBeenCalledWith(detalles);
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

      describe('trackLotesById', () => {
        it('Should return tracked Lotes primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLotesById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
