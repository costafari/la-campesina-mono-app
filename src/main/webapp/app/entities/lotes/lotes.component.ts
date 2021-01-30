import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILotes } from 'app/shared/model/lotes.model';
import { LotesService } from './lotes.service';
import { LotesDeleteDialogComponent } from './lotes-delete-dialog.component';

@Component({
  selector: 'jhi-lotes',
  templateUrl: './lotes.component.html',
})
export class LotesComponent implements OnInit, OnDestroy {
  lotes?: ILotes[];
  eventSubscriber?: Subscription;

  constructor(protected lotesService: LotesService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.lotesService.query().subscribe((res: HttpResponse<ILotes[]>) => (this.lotes = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInLotes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ILotes): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInLotes(): void {
    this.eventSubscriber = this.eventManager.subscribe('lotesListModification', () => this.loadAll());
  }

  delete(lotes: ILotes): void {
    const modalRef = this.modalService.open(LotesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.lotes = lotes;
  }
}
