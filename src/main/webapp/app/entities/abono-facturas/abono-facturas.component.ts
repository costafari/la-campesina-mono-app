import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAbonoFacturas } from 'app/shared/model/abono-facturas.model';
import { AbonoFacturasService } from './abono-facturas.service';
import { AbonoFacturasDeleteDialogComponent } from './abono-facturas-delete-dialog.component';

@Component({
  selector: 'jhi-abono-facturas',
  templateUrl: './abono-facturas.component.html',
})
export class AbonoFacturasComponent implements OnInit, OnDestroy {
  abonoFacturas?: IAbonoFacturas[];
  eventSubscriber?: Subscription;

  constructor(
    protected abonoFacturasService: AbonoFacturasService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.abonoFacturasService.query().subscribe((res: HttpResponse<IAbonoFacturas[]>) => (this.abonoFacturas = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInAbonoFacturas();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IAbonoFacturas): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAbonoFacturas(): void {
    this.eventSubscriber = this.eventManager.subscribe('abonoFacturasListModification', () => this.loadAll());
  }

  delete(abonoFacturas: IAbonoFacturas): void {
    const modalRef = this.modalService.open(AbonoFacturasDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.abonoFacturas = abonoFacturas;
  }
}
