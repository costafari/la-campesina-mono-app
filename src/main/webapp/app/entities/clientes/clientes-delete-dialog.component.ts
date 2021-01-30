import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IClientes } from 'app/shared/model/clientes.model';
import { ClientesService } from './clientes.service';

@Component({
  templateUrl: './clientes-delete-dialog.component.html',
})
export class ClientesDeleteDialogComponent {
  clientes?: IClientes;

  constructor(protected clientesService: ClientesService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.clientesService.delete(id).subscribe(() => {
      this.eventManager.broadcast('clientesListModification');
      this.activeModal.close();
    });
  }
}
