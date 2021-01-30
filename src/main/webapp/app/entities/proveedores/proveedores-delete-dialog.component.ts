import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProveedores } from 'app/shared/model/proveedores.model';
import { ProveedoresService } from './proveedores.service';

@Component({
  templateUrl: './proveedores-delete-dialog.component.html',
})
export class ProveedoresDeleteDialogComponent {
  proveedores?: IProveedores;

  constructor(
    protected proveedoresService: ProveedoresService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.proveedoresService.delete(id).subscribe(() => {
      this.eventManager.broadcast('proveedoresListModification');
      this.activeModal.close();
    });
  }
}
