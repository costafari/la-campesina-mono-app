import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAbonoFacturas } from 'app/shared/model/abono-facturas.model';
import { AbonoFacturasService } from './abono-facturas.service';

@Component({
  templateUrl: './abono-facturas-delete-dialog.component.html',
})
export class AbonoFacturasDeleteDialogComponent {
  abonoFacturas?: IAbonoFacturas;

  constructor(
    protected abonoFacturasService: AbonoFacturasService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.abonoFacturasService.delete(id).subscribe(() => {
      this.eventManager.broadcast('abonoFacturasListModification');
      this.activeModal.close();
    });
  }
}
