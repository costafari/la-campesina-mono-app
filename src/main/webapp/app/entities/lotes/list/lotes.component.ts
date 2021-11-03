import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILotes } from '../lotes.model';
import { LotesService } from '../service/lotes.service';
import { LotesDeleteDialogComponent } from '../delete/lotes-delete-dialog.component';

@Component({
  selector: 'jhi-lotes',
  templateUrl: './lotes.component.html',
})
export class LotesComponent implements OnInit {
  lotes?: ILotes[];
  isLoading = false;

  constructor(protected lotesService: LotesService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.lotesService.query().subscribe(
      (res: HttpResponse<ILotes[]>) => {
        this.isLoading = false;
        this.lotes = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ILotes): number {
    return item.id!;
  }

  delete(lotes: ILotes): void {
    const modalRef = this.modalService.open(LotesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.lotes = lotes;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
