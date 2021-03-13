import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFacturasDetalle } from 'app/shared/model/facturas-detalle.model';
import { FacturasDetalleService } from './facturas-detalle.service';

@Component({
  templateUrl: './facturas-detalle-delete-dialog.component.html',
})
export class FacturasDetalleDeleteDialogComponent {
  facturasDetalle?: IFacturasDetalle;

  constructor(
    protected facturasDetalleService: FacturasDetalleService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.facturasDetalleService.delete(id).subscribe(() => {
      this.eventManager.broadcast('facturasDetalleListModification');
      this.activeModal.close();
    });
  }
}
