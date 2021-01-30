import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProductos } from 'app/shared/model/productos.model';
import { ProductosService } from './productos.service';

@Component({
  templateUrl: './productos-delete-dialog.component.html',
})
export class ProductosDeleteDialogComponent {
  productos?: IProductos;

  constructor(protected productosService: ProductosService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productosService.delete(id).subscribe(() => {
      this.eventManager.broadcast('productosListModification');
      this.activeModal.close();
    });
  }
}
