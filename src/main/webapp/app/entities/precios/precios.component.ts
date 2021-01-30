import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrecios } from 'app/shared/model/precios.model';
import { PreciosService } from './precios.service';
import { PreciosDeleteDialogComponent } from './precios-delete-dialog.component';

@Component({
  selector: 'jhi-precios',
  templateUrl: './precios.component.html',
})
export class PreciosComponent implements OnInit, OnDestroy {
  precios?: IPrecios[];
  eventSubscriber?: Subscription;

  constructor(protected preciosService: PreciosService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.preciosService.query().subscribe((res: HttpResponse<IPrecios[]>) => (this.precios = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPrecios();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPrecios): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPrecios(): void {
    this.eventSubscriber = this.eventManager.subscribe('preciosListModification', () => this.loadAll());
  }

  delete(precios: IPrecios): void {
    const modalRef = this.modalService.open(PreciosDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.precios = precios;
  }
}
