import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LacampesinaTestModule } from '../../../test.module';
import { GeneralidadesUpdateComponent } from 'app/entities/generalidades/generalidades-update.component';
import { GeneralidadesService } from 'app/entities/generalidades/generalidades.service';
import { Generalidades } from 'app/shared/model/generalidades.model';

describe('Component Tests', () => {
  describe('Generalidades Management Update Component', () => {
    let comp: GeneralidadesUpdateComponent;
    let fixture: ComponentFixture<GeneralidadesUpdateComponent>;
    let service: GeneralidadesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LacampesinaTestModule],
        declarations: [GeneralidadesUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(GeneralidadesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GeneralidadesUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GeneralidadesService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Generalidades(123);
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
        const entity = new Generalidades();
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
