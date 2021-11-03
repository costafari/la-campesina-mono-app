jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProveedoresService } from '../service/proveedores.service';
import { IProveedores, Proveedores } from '../proveedores.model';
import { ILotes } from 'app/entities/lotes/lotes.model';
import { LotesService } from 'app/entities/lotes/service/lotes.service';

import { ProveedoresUpdateComponent } from './proveedores-update.component';

describe('Component Tests', () => {
  describe('Proveedores Management Update Component', () => {
    let comp: ProveedoresUpdateComponent;
    let fixture: ComponentFixture<ProveedoresUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let proveedoresService: ProveedoresService;
    let lotesService: LotesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProveedoresUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ProveedoresUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProveedoresUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      proveedoresService = TestBed.inject(ProveedoresService);
      lotesService = TestBed.inject(LotesService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call lotes query and add missing value', () => {
        const proveedores: IProveedores = { id: 456 };
        const lotes: ILotes = { id: 99139 };
        proveedores.lotes = lotes;

        const lotesCollection: ILotes[] = [{ id: 14431 }];
        jest.spyOn(lotesService, 'query').mockReturnValue(of(new HttpResponse({ body: lotesCollection })));
        const expectedCollection: ILotes[] = [lotes, ...lotesCollection];
        jest.spyOn(lotesService, 'addLotesToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ proveedores });
        comp.ngOnInit();

        expect(lotesService.query).toHaveBeenCalled();
        expect(lotesService.addLotesToCollectionIfMissing).toHaveBeenCalledWith(lotesCollection, lotes);
        expect(comp.lotesCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const proveedores: IProveedores = { id: 456 };
        const lotes: ILotes = { id: 41886 };
        proveedores.lotes = lotes;

        activatedRoute.data = of({ proveedores });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(proveedores));
        expect(comp.lotesCollection).toContain(lotes);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Proveedores>>();
        const proveedores = { id: 123 };
        jest.spyOn(proveedoresService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ proveedores });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: proveedores }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(proveedoresService.update).toHaveBeenCalledWith(proveedores);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Proveedores>>();
        const proveedores = new Proveedores();
        jest.spyOn(proveedoresService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ proveedores });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: proveedores }));
        saveSubject.complete();

        // THEN
        expect(proveedoresService.create).toHaveBeenCalledWith(proveedores);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Proveedores>>();
        const proveedores = { id: 123 };
        jest.spyOn(proveedoresService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ proveedores });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(proveedoresService.update).toHaveBeenCalledWith(proveedores);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
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
