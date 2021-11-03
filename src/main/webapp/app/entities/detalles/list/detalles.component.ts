import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDetalles } from '../detalles.model';
import { DetallesService } from '../service/detalles.service';
import { DetallesDeleteDialogComponent } from '../delete/detalles-delete-dialog.component';

@Component({
  selector: 'jhi-detalles',
  templateUrl: './detalles.component.html',
})
export class DetallesComponent implements OnInit {
  detalles?: IDetalles[];
  isLoading = false;

  constructor(protected detallesService: DetallesService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.detallesService.query().subscribe(
      (res: HttpResponse<IDetalles[]>) => {
        this.isLoading = false;
        this.detalles = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDetalles): number {
    return item.id!;
  }

  delete(detalles: IDetalles): void {
    const modalRef = this.modalService.open(DetallesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.detalles = detalles;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
