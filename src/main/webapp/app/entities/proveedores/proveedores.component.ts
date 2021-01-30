import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProveedores } from 'app/shared/model/proveedores.model';
import { ProveedoresService } from './proveedores.service';
import { ProveedoresDeleteDialogComponent } from './proveedores-delete-dialog.component';

@Component({
  selector: 'jhi-proveedores',
  templateUrl: './proveedores.component.html',
})
export class ProveedoresComponent implements OnInit, OnDestroy {
  proveedores?: IProveedores[];
  eventSubscriber?: Subscription;

  constructor(
    protected proveedoresService: ProveedoresService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.proveedoresService.query().subscribe((res: HttpResponse<IProveedores[]>) => (this.proveedores = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInProveedores();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IProveedores): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInProveedores(): void {
    this.eventSubscriber = this.eventManager.subscribe('proveedoresListModification', () => this.loadAll());
  }

  delete(proveedores: IProveedores): void {
    const modalRef = this.modalService.open(ProveedoresDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.proveedores = proveedores;
  }
}
