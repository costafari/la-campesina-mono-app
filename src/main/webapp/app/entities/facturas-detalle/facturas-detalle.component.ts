import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFacturasDetalle } from 'app/shared/model/facturas-detalle.model';
import { FacturasDetalleService } from './facturas-detalle.service';
import { FacturasDetalleDeleteDialogComponent } from './facturas-detalle-delete-dialog.component';

@Component({
  selector: 'jhi-facturas-detalle',
  templateUrl: './facturas-detalle.component.html',
})
export class FacturasDetalleComponent implements OnInit, OnDestroy {
  facturasDetalles?: IFacturasDetalle[];
  eventSubscriber?: Subscription;

  constructor(
    protected facturasDetalleService: FacturasDetalleService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.facturasDetalleService.query().subscribe((res: HttpResponse<IFacturasDetalle[]>) => (this.facturasDetalles = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFacturasDetalles();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IFacturasDetalle): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInFacturasDetalles(): void {
    this.eventSubscriber = this.eventManager.subscribe('facturasDetalleListModification', () => this.loadAll());
  }

  delete(facturasDetalle: IFacturasDetalle): void {
    const modalRef = this.modalService.open(FacturasDetalleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.facturasDetalle = facturasDetalle;
  }
}
