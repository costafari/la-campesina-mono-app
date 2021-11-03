import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAbonos } from '../abonos.model';
import { AbonosService } from '../service/abonos.service';
import { AbonosDeleteDialogComponent } from '../delete/abonos-delete-dialog.component';

@Component({
  selector: 'jhi-abonos',
  templateUrl: './abonos.component.html',
})
export class AbonosComponent implements OnInit {
  abonos?: IAbonos[];
  isLoading = false;

  constructor(protected abonosService: AbonosService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.abonosService.query().subscribe(
      (res: HttpResponse<IAbonos[]>) => {
        this.isLoading = false;
        this.abonos = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAbonos): number {
    return item.id!;
  }

  delete(abonos: IAbonos): void {
    const modalRef = this.modalService.open(AbonosDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.abonos = abonos;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
