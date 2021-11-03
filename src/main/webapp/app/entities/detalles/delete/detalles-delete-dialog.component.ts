import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDetalles } from '../detalles.model';
import { DetallesService } from '../service/detalles.service';

@Component({
  templateUrl: './detalles-delete-dialog.component.html',
})
export class DetallesDeleteDialogComponent {
  detalles?: IDetalles;

  constructor(protected detallesService: DetallesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.detallesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
