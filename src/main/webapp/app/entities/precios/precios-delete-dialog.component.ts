import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPrecios } from 'app/shared/model/precios.model';
import { PreciosService } from './precios.service';

@Component({
  templateUrl: './precios-delete-dialog.component.html',
})
export class PreciosDeleteDialogComponent {
  precios?: IPrecios;

  constructor(protected preciosService: PreciosService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.preciosService.delete(id).subscribe(() => {
      this.eventManager.broadcast('preciosListModification');
      this.activeModal.close();
    });
  }
}
