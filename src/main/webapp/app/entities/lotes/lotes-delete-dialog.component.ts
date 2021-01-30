import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILotes } from 'app/shared/model/lotes.model';
import { LotesService } from './lotes.service';

@Component({
  templateUrl: './lotes-delete-dialog.component.html',
})
export class LotesDeleteDialogComponent {
  lotes?: ILotes;

  constructor(protected lotesService: LotesService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lotesService.delete(id).subscribe(() => {
      this.eventManager.broadcast('lotesListModification');
      this.activeModal.close();
    });
  }
}
