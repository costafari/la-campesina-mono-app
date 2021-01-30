import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITickets } from 'app/shared/model/tickets.model';
import { TicketsService } from './tickets.service';

@Component({
  templateUrl: './tickets-delete-dialog.component.html',
})
export class TicketsDeleteDialogComponent {
  tickets?: ITickets;

  constructor(protected ticketsService: TicketsService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ticketsService.delete(id).subscribe(() => {
      this.eventManager.broadcast('ticketsListModification');
      this.activeModal.close();
    });
  }
}
