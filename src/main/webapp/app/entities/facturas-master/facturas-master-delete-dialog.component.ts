import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFacturasMaster } from 'app/shared/model/facturas-master.model';
import { FacturasMasterService } from './facturas-master.service';

@Component({
  templateUrl: './facturas-master-delete-dialog.component.html',
})
export class FacturasMasterDeleteDialogComponent {
  facturasMaster?: IFacturasMaster;

  constructor(
    protected facturasMasterService: FacturasMasterService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.facturasMasterService.delete(id).subscribe(() => {
      this.eventManager.broadcast('facturasMasterListModification');
      this.activeModal.close();
    });
  }
}
