import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LacampesinaTestModule } from '../../../test.module';
import { LotesUpdateComponent } from 'app/entities/lotes/lotes-update.component';
import { LotesService } from 'app/entities/lotes/lotes.service';
import { Lotes } from 'app/shared/model/lotes.model';

describe('Component Tests', () => {
  describe('Lotes Management Update Component', () => {
    let comp: LotesUpdateComponent;
    let fixture: ComponentFixture<LotesUpdateComponent>;
    let service: LotesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LacampesinaTestModule],
        declarations: [LotesUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(LotesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LotesUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LotesService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Lotes(123);
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
        const entity = new Lotes();
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
