import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { LacampesinaTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { FacturasDetalleDeleteDialogComponent } from 'app/entities/facturas-detalle/facturas-detalle-delete-dialog.component';
import { FacturasDetalleService } from 'app/entities/facturas-detalle/facturas-detalle.service';

describe('Component Tests', () => {
  describe('FacturasDetalle Management Delete Component', () => {
    let comp: FacturasDetalleDeleteDialogComponent;
    let fixture: ComponentFixture<FacturasDetalleDeleteDialogComponent>;
    let service: FacturasDetalleService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LacampesinaTestModule],
        declarations: [FacturasDetalleDeleteDialogComponent],
      })
        .overrideTemplate(FacturasDetalleDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FacturasDetalleDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FacturasDetalleService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
