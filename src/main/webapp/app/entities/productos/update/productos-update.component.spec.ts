jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProductosService } from '../service/productos.service';
import { IProductos, Productos } from '../productos.model';

import { ProductosUpdateComponent } from './productos-update.component';

describe('Component Tests', () => {
  describe('Productos Management Update Component', () => {
    let comp: ProductosUpdateComponent;
    let fixture: ComponentFixture<ProductosUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let productosService: ProductosService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProductosUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ProductosUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProductosUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      productosService = TestBed.inject(ProductosService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const productos: IProductos = { id: 456 };

        activatedRoute.data = of({ productos });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(productos));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Productos>>();
        const productos = { id: 123 };
        jest.spyOn(productosService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ productos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: productos }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(productosService.update).toHaveBeenCalledWith(productos);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Productos>>();
        const productos = new Productos();
        jest.spyOn(productosService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ productos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: productos }));
        saveSubject.complete();

        // THEN
        expect(productosService.create).toHaveBeenCalledWith(productos);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Productos>>();
        const productos = { id: 123 };
        jest.spyOn(productosService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ productos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(productosService.update).toHaveBeenCalledWith(productos);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
