import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LacampesinaTestModule } from '../../../test.module';
import { PreciosUpdateComponent } from 'app/entities/precios/precios-update.component';
import { PreciosService } from 'app/entities/precios/precios.service';
import { Precios } from 'app/shared/model/precios.model';

describe('Component Tests', () => {
  describe('Precios Management Update Component', () => {
    let comp: PreciosUpdateComponent;
    let fixture: ComponentFixture<PreciosUpdateComponent>;
    let service: PreciosService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LacampesinaTestModule],
        declarations: [PreciosUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PreciosUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PreciosUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PreciosService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Precios(123);
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
        const entity = new Precios();
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
