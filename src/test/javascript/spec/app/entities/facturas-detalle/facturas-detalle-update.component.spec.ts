import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LacampesinaTestModule } from '../../../test.module';
import { FacturasDetalleUpdateComponent } from 'app/entities/facturas-detalle/facturas-detalle-update.component';
import { FacturasDetalleService } from 'app/entities/facturas-detalle/facturas-detalle.service';
import { FacturasDetalle } from 'app/shared/model/facturas-detalle.model';

describe('Component Tests', () => {
  describe('FacturasDetalle Management Update Component', () => {
    let comp: FacturasDetalleUpdateComponent;
    let fixture: ComponentFixture<FacturasDetalleUpdateComponent>;
    let service: FacturasDetalleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LacampesinaTestModule],
        declarations: [FacturasDetalleUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(FacturasDetalleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FacturasDetalleUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FacturasDetalleService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new FacturasDetalle(123);
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
        const entity = new FacturasDetalle();
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
