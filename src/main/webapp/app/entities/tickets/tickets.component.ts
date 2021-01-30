import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITickets } from 'app/shared/model/tickets.model';
import { TicketsService } from './tickets.service';
import { TicketsDeleteDialogComponent } from './tickets-delete-dialog.component';

@Component({
  selector: 'jhi-tickets',
  templateUrl: './tickets.component.html',
})
export class TicketsComponent implements OnInit, OnDestroy {
  tickets?: ITickets[];
  eventSubscriber?: Subscription;

  constructor(protected ticketsService: TicketsService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.ticketsService.query().subscribe((res: HttpResponse<ITickets[]>) => (this.tickets = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTickets();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITickets): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInTickets(): void {
    this.eventSubscriber = this.eventManager.subscribe('ticketsListModification', () => this.loadAll());
  }

  delete(tickets: ITickets): void {
    const modalRef = this.modalService.open(TicketsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tickets = tickets;
  }
}
