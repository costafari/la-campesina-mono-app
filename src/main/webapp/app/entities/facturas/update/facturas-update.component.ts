import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFacturas, Facturas } from '../facturas.model';
import { FacturasService } from '../service/facturas.service';
import { IClientes } from 'app/entities/clientes/clientes.model';
import { ClientesService } from 'app/entities/clientes/service/clientes.service';
import { IDetalles } from 'app/entities/detalles/detalles.model';
import { DetallesService } from 'app/entities/detalles/service/detalles.service';
import { DetallesDeleteDialogComponent } from 'app/entities/detalles/delete/detalles-delete-dialog.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-facturas-update',
  templateUrl: './facturas-update.component.html',
})
export class FacturasUpdateComponent implements OnInit {
  isSaving = false;

  clientesSharedCollection: IClientes[] = [];

  // VARIABLES DE DETALLES
  detalles?: IDetalles[];
  isLoading = false;
  //

  //
  facturaValue: IFacturas = new Facturas();

  editForm = this.fb.group({
    id: [],
    numeroFactura: [null, [Validators.required]],
    fechaFactura: [],
    condicionPago: [],
    clientes: [],
  });

  constructor(
    protected facturasService: FacturasService,
    protected clientesService: ClientesService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    // Variables de DETALLES
    protected detallesService: DetallesService,
    protected modalService: NgbModal //
  ) {}

  // VARIABLES DE DETALLES

  // this.bookService.query({'title.equals':someValue}).subscribe(...);

  loadAll(facturas: IFacturas): void {
    this.isLoading = true;

    if (this.facturaValue.id != undefined) {
      this.detallesService.query({ 'facturasId.equals': facturas.id }).subscribe(
        (res: HttpResponse<IDetalles[]>) => {
          this.isLoading = false;
          this.detalles = res.body ?? [];
        },
        () => {
          this.isLoading = false;
        }
      );
    }
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
        this.loadAll(this.facturaValue);
      }
    });
  }

  //

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facturas }) => {
      if (facturas.id === undefined) {
        const today = dayjs().startOf('day');
        facturas.fechaFactura = today;
      }

      this.updateForm(facturas);

      this.loadRelationshipsOptions();

      this.facturaValue = facturas;

      this.loadAll(this.facturaValue);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const facturas = this.createFromForm();
    this.facturaValue = facturas;
    if (facturas.id !== undefined) {
      this.subscribeToSaveResponse(this.facturasService.update(facturas));
    } else {
      this.subscribeToSaveResponse(this.facturasService.create(facturas));
    }
  }

  trackClientesById(index: number, item: IClientes): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFacturas>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    // this.previousState();

    this.isLoading = true;
    let facturaRes: IFacturas[];

    this.facturasService.query({ 'numeroFactura.equals': this.facturaValue.numeroFactura }).subscribe(
      (res: HttpResponse<IFacturas[]>) => {
        this.isLoading = false;
        facturaRes = res.body ?? [];
        this.facturaValue = facturaRes[0];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(facturas: IFacturas): void {
    this.editForm.patchValue({
      id: facturas.id,
      numeroFactura: facturas.numeroFactura,
      fechaFactura: facturas.fechaFactura ? facturas.fechaFactura.format(DATE_TIME_FORMAT) : null,
      condicionPago: facturas.condicionPago,
      clientes: facturas.clientes,
    });

    this.clientesSharedCollection = this.clientesService.addClientesToCollectionIfMissing(this.clientesSharedCollection, facturas.clientes);
  }

  protected loadRelationshipsOptions(): void {
    this.clientesService
      .query()
      .pipe(map((res: HttpResponse<IClientes[]>) => res.body ?? []))
      .pipe(
        map((clientes: IClientes[]) =>
          this.clientesService.addClientesToCollectionIfMissing(clientes, this.editForm.get('clientes')!.value)
        )
      )
      .subscribe((clientes: IClientes[]) => (this.clientesSharedCollection = clientes));
  }

  protected createFromForm(): IFacturas {
    return {
      ...new Facturas(),
      id: this.editForm.get(['id'])!.value,
      numeroFactura: this.editForm.get(['numeroFactura'])!.value,
      fechaFactura: this.editForm.get(['fechaFactura'])!.value
        ? dayjs(this.editForm.get(['fechaFactura'])!.value, DATE_TIME_FORMAT)
        : undefined,
      condicionPago: this.editForm.get(['condicionPago'])!.value,
      clientes: this.editForm.get(['clientes'])!.value,
    };
  }
}
