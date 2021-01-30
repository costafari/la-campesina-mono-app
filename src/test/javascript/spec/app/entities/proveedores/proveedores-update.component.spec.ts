import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LacampesinaTestModule } from '../../../test.module';
import { ProveedoresUpdateComponent } from 'app/entities/proveedores/proveedores-update.component';
import { ProveedoresService } from 'app/entities/proveedores/proveedores.service';
import { Proveedores } from 'app/shared/model/proveedores.model';

describe('Component Tests', () => {
  describe('Proveedores Management Update Component', () => {
    let comp: ProveedoresUpdateComponent;
    let fixture: ComponentFixture<ProveedoresUpdateComponent>;
    let service: ProveedoresService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LacampesinaTestModule],
        declarations: [ProveedoresUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ProveedoresUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProveedoresUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProveedoresService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Proveedores(123);
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
        const entity = new Proveedores();
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
