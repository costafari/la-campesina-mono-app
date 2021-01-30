import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGeneralidades } from 'app/shared/model/generalidades.model';
import { GeneralidadesService } from './generalidades.service';
import { GeneralidadesDeleteDialogComponent } from './generalidades-delete-dialog.component';

@Component({
  selector: 'jhi-generalidades',
  templateUrl: './generalidades.component.html',
})
export class GeneralidadesComponent implements OnInit, OnDestroy {
  generalidades?: IGeneralidades[];
  eventSubscriber?: Subscription;

  constructor(
    protected generalidadesService: GeneralidadesService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.generalidadesService.query().subscribe((res: HttpResponse<IGeneralidades[]>) => (this.generalidades = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInGeneralidades();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IGeneralidades): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInGeneralidades(): void {
    this.eventSubscriber = this.eventManager.subscribe('generalidadesListModification', () => this.loadAll());
  }

  delete(generalidades: IGeneralidades): void {
    const modalRef = this.modalService.open(GeneralidadesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.generalidades = generalidades;
  }
}
