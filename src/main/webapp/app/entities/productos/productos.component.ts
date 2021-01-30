import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProductos } from 'app/shared/model/productos.model';
import { ProductosService } from './productos.service';
import { ProductosDeleteDialogComponent } from './productos-delete-dialog.component';

@Component({
  selector: 'jhi-productos',
  templateUrl: './productos.component.html',
})
export class ProductosComponent implements OnInit, OnDestroy {
  productos?: IProductos[];
  eventSubscriber?: Subscription;

  constructor(protected productosService: ProductosService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.productosService.query().subscribe((res: HttpResponse<IProductos[]>) => (this.productos = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInProductos();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IProductos): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInProductos(): void {
    this.eventSubscriber = this.eventManager.subscribe('productosListModification', () => this.loadAll());
  }

  delete(productos: IProductos): void {
    const modalRef = this.modalService.open(ProductosDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.productos = productos;
  }
}
