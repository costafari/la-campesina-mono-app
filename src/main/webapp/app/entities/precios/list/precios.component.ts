import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrecios } from '../precios.model';
import { PreciosService } from '../service/precios.service';
import { PreciosDeleteDialogComponent } from '../delete/precios-delete-dialog.component';

@Component({
  selector: 'jhi-precios',
  templateUrl: './precios.component.html',
})
export class PreciosComponent implements OnInit {
  precios?: IPrecios[];
  isLoading = false;

  constructor(protected preciosService: PreciosService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.preciosService.query().subscribe(
      (res: HttpResponse<IPrecios[]>) => {
        this.isLoading = false;
        this.precios = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPrecios): number {
    return item.id!;
  }

  delete(precios: IPrecios): void {
    const modalRef = this.modalService.open(PreciosDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.precios = precios;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
