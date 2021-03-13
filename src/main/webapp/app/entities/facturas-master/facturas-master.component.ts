import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFacturasMaster } from 'app/shared/model/facturas-master.model';
import { FacturasMasterService } from './facturas-master.service';
import { FacturasMasterDeleteDialogComponent } from './facturas-master-delete-dialog.component';

@Component({
  selector: 'jhi-facturas-master',
  templateUrl: './facturas-master.component.html',
})
export class FacturasMasterComponent implements OnInit, OnDestroy {
  facturasMasters?: IFacturasMaster[];
  eventSubscriber?: Subscription;

  constructor(
    protected facturasMasterService: FacturasMasterService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.facturasMasterService.query().subscribe((res: HttpResponse<IFacturasMaster[]>) => (this.facturasMasters = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFacturasMasters();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IFacturasMaster): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInFacturasMasters(): void {
    this.eventSubscriber = this.eventManager.subscribe('facturasMasterListModification', () => this.loadAll());
  }

  delete(facturasMaster: IFacturasMaster): void {
    const modalRef = this.modalService.open(FacturasMasterDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.facturasMaster = facturasMaster;
  }
}
