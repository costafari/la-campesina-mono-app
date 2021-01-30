import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IClientes } from 'app/shared/model/clientes.model';
import { ClientesService } from './clientes.service';
import { ClientesDeleteDialogComponent } from './clientes-delete-dialog.component';

@Component({
  selector: 'jhi-clientes',
  templateUrl: './clientes.component.html',
})
export class ClientesComponent implements OnInit, OnDestroy {
  clientes?: IClientes[];
  eventSubscriber?: Subscription;

  constructor(protected clientesService: ClientesService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.clientesService.query().subscribe((res: HttpResponse<IClientes[]>) => (this.clientes = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInClientes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IClientes): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInClientes(): void {
    this.eventSubscriber = this.eventManager.subscribe('clientesListModification', () => this.loadAll());
  }

  delete(clientes: IClientes): void {
    const modalRef = this.modalService.open(ClientesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.clientes = clientes;
  }
}
