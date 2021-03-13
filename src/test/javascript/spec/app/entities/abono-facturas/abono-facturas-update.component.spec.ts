import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LacampesinaTestModule } from '../../../test.module';
import { AbonoFacturasUpdateComponent } from 'app/entities/abono-facturas/abono-facturas-update.component';
import { AbonoFacturasService } from 'app/entities/abono-facturas/abono-facturas.service';
import { AbonoFacturas } from 'app/shared/model/abono-facturas.model';

describe('Component Tests', () => {
  describe('AbonoFacturas Management Update Component', () => {
    let comp: AbonoFacturasUpdateComponent;
    let fixture: ComponentFixture<AbonoFacturasUpdateComponent>;
    let service: AbonoFacturasService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LacampesinaTestModule],
        declarations: [AbonoFacturasUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(AbonoFacturasUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AbonoFacturasUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AbonoFacturasService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new AbonoFacturas(123);
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
        const entity = new AbonoFacturas();
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
