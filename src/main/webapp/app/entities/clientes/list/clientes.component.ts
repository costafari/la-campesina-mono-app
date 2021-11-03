import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IClientes } from '../clientes.model';
import { ClientesService } from '../service/clientes.service';
import { ClientesDeleteDialogComponent } from '../delete/clientes-delete-dialog.component';

@Component({
  selector: 'jhi-clientes',
  templateUrl: './clientes.component.html',
})
export class ClientesComponent implements OnInit {
  clientes?: IClientes[];
  isLoading = false;

  constructor(protected clientesService: ClientesService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.clientesService.query().subscribe(
      (res: HttpResponse<IClientes[]>) => {
        this.isLoading = false;
        this.clientes = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IClientes): number {
    return item.id!;
  }

  delete(clientes: IClientes): void {
    const modalRef = this.modalService.open(ClientesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.clientes = clientes;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
