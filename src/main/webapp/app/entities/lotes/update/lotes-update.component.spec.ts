jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LotesService } from '../service/lotes.service';
import { ILotes, Lotes } from '../lotes.model';
import { IProductos } from 'app/entities/productos/productos.model';
import { ProductosService } from 'app/entities/productos/service/productos.service';
import { IPrecios } from 'app/entities/precios/precios.model';
import { PreciosService } from 'app/entities/precios/service/precios.service';

import { LotesUpdateComponent } from './lotes-update.component';

describe('Component Tests', () => {
  describe('Lotes Management Update Component', () => {
    let comp: LotesUpdateComponent;
    let fixture: ComponentFixture<LotesUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let lotesService: LotesService;
    let productosService: ProductosService;
    let preciosService: PreciosService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LotesUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LotesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LotesUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      lotesService = TestBed.inject(LotesService);
      productosService = TestBed.inject(ProductosService);
      preciosService = TestBed.inject(PreciosService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Productos query and add missing value', () => {
        const lotes: ILotes = { id: 456 };
        const productos: IProductos = { id: 53715 };
        lotes.productos = productos;

        const productosCollection: IProductos[] = [{ id: 53723 }];
        jest.spyOn(productosService, 'query').mockReturnValue(of(new HttpResponse({ body: productosCollection })));
        const additionalProductos = [productos];
        const expectedCollection: IProductos[] = [...additionalProductos, ...productosCollection];
        jest.spyOn(productosService, 'addProductosToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lotes });
        comp.ngOnInit();

        expect(productosService.query).toHaveBeenCalled();
        expect(productosService.addProductosToCollectionIfMissing).toHaveBeenCalledWith(productosCollection, ...additionalProductos);
        expect(comp.productosSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Precios query and add missing value', () => {
        const lotes: ILotes = { id: 456 };
        const precios: IPrecios = { id: 78348 };
        lotes.precios = precios;

        const preciosCollection: IPrecios[] = [{ id: 51491 }];
        jest.spyOn(preciosService, 'query').mockReturnValue(of(new HttpResponse({ body: preciosCollection })));
        const additionalPrecios = [precios];
        const expectedCollection: IPrecios[] = [...additionalPrecios, ...preciosCollection];
        jest.spyOn(preciosService, 'addPreciosToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lotes });
        comp.ngOnInit();

        expect(preciosService.query).toHaveBeenCalled();
        expect(preciosService.addPreciosToCollectionIfMissing).toHaveBeenCalledWith(preciosCollection, ...additionalPrecios);
        expect(comp.preciosSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const lotes: ILotes = { id: 456 };
        const productos: IProductos = { id: 40227 };
        lotes.productos = productos;
        const precios: IPrecios = { id: 70831 };
        lotes.precios = precios;

        activatedRoute.data = of({ lotes });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(lotes));
        expect(comp.productosSharedCollection).toContain(productos);
        expect(comp.preciosSharedCollection).toContain(precios);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Lotes>>();
        const lotes = { id: 123 };
        jest.spyOn(lotesService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lotes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lotes }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(lotesService.update).toHaveBeenCalledWith(lotes);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Lotes>>();
        const lotes = new Lotes();
        jest.spyOn(lotesService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lotes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lotes }));
        saveSubject.complete();

        // THEN
        expect(lotesService.create).toHaveBeenCalledWith(lotes);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Lotes>>();
        const lotes = { id: 123 };
        jest.spyOn(lotesService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lotes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(lotesService.update).toHaveBeenCalledWith(lotes);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackProductosById', () => {
        it('Should return tracked Productos primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackProductosById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackPreciosById', () => {
        it('Should return tracked Precios primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPreciosById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
