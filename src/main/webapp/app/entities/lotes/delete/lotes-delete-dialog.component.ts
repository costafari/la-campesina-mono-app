import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILotes } from '../lotes.model';
import { LotesService } from '../service/lotes.service';

@Component({
  templateUrl: './lotes-delete-dialog.component.html',
})
export class LotesDeleteDialogComponent {
  lotes?: ILotes;

  constructor(protected lotesService: LotesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lotesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
