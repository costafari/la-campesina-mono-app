import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAbonos } from '../abonos.model';
import { AbonosService } from '../service/abonos.service';

@Component({
  templateUrl: './abonos-delete-dialog.component.html',
})
export class AbonosDeleteDialogComponent {
  abonos?: IAbonos;

  constructor(protected abonosService: AbonosService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.abonosService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
