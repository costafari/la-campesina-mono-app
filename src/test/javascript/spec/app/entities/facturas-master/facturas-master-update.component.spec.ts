import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LacampesinaTestModule } from '../../../test.module';
import { FacturasMasterUpdateComponent } from 'app/entities/facturas-master/facturas-master-update.component';
import { FacturasMasterService } from 'app/entities/facturas-master/facturas-master.service';
import { FacturasMaster } from 'app/shared/model/facturas-master.model';

describe('Component Tests', () => {
  describe('FacturasMaster Management Update Component', () => {
    let comp: FacturasMasterUpdateComponent;
    let fixture: ComponentFixture<FacturasMasterUpdateComponent>;
    let service: FacturasMasterService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LacampesinaTestModule],
        declarations: [FacturasMasterUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(FacturasMasterUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FacturasMasterUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FacturasMasterService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new FacturasMaster(123);
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
        const entity = new FacturasMaster();
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
