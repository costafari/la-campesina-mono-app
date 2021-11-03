import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProveedores } from '../proveedores.model';
import { ProveedoresService } from '../service/proveedores.service';
import { ProveedoresDeleteDialogComponent } from '../delete/proveedores-delete-dialog.component';

@Component({
  selector: 'jhi-proveedores',
  templateUrl: './proveedores.component.html',
})
export class ProveedoresComponent implements OnInit {
  proveedores?: IProveedores[];
  isLoading = false;

  constructor(protected proveedoresService: ProveedoresService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.proveedoresService.query().subscribe(
      (res: HttpResponse<IProveedores[]>) => {
        this.isLoading = false;
        this.proveedores = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IProveedores): number {
    return item.id!;
  }

  delete(proveedores: IProveedores): void {
    const modalRef = this.modalService.open(ProveedoresDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.proveedores = proveedores;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
