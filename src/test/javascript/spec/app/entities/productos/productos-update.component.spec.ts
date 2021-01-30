import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LacampesinaTestModule } from '../../../test.module';
import { ProductosUpdateComponent } from 'app/entities/productos/productos-update.component';
import { ProductosService } from 'app/entities/productos/productos.service';
import { Productos } from 'app/shared/model/productos.model';

describe('Component Tests', () => {
  describe('Productos Management Update Component', () => {
    let comp: ProductosUpdateComponent;
    let fixture: ComponentFixture<ProductosUpdateComponent>;
    let service: ProductosService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LacampesinaTestModule],
        declarations: [ProductosUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ProductosUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProductosUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProductosService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Productos(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Productos();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
