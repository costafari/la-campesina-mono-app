import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrecios } from '../precios.model';
import { PreciosService } from '../service/precios.service';

@Component({
  templateUrl: './precios-delete-dialog.component.html',
})
export class PreciosDeleteDialogComponent {
  precios?: IPrecios;

  constructor(protected preciosService: PreciosService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.preciosService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
