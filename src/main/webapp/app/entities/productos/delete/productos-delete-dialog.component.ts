import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProductos } from '../productos.model';
import { ProductosService } from '../service/productos.service';

@Component({
  templateUrl: './productos-delete-dialog.component.html',
})
export class ProductosDeleteDialogComponent {
  productos?: IProductos;

  constructor(protected productosService: ProductosService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productosService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
