import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormArray, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map, switchMap } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFacturas, Facturas } from '../facturas.model';
import { FacturasService } from '../service/facturas.service';
import { IClientes } from 'app/entities/clientes/clientes.model';
import { ClientesService } from 'app/entities/clientes/service/clientes.service';
import { LotesService } from 'app/entities/lotes/service/lotes.service';
import { ILotes } from 'app/entities/lotes/lotes.model';
import { IProductos } from 'app/entities/productos/productos.model';

@Component({
  selector: 'jhi-facturas-update',
  templateUrl: './facturas-update.component.html',
})
export class FacturasUpdateComponent implements OnInit {
  isSaving = false;

  clientesSharedCollection: IClientes[] = [];
  lotesSharedCollection: ILotes[] = [];
  lotesList: ILotes[] = [];
  productosList: IProductos[] = [];

  editForm = this.fb.group({
    id: [],
    numeroFactura: [null, [Validators.required]],
    fechaFactura: [],
    condicionPago: [],
    clientes: [],
  });

  detallesFrm = this.fbd.group({
    itemsDetalles: this.fbd.array([]),
  });

  detallesForm = this.fbd.group({
    id: [],
    cantidad: [],
    total: [],
    lotes: [],
    factura: [],
    productos: [],
  });

  constructor(
    protected facturasService: FacturasService,
    protected clientesService: ClientesService,
    protected lotesService: LotesService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected fbd: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facturas }) => {
      if (facturas.id === undefined) {
        const today = dayjs().startOf('day');
        facturas.fechaFactura = today;
      }

      this.updateForm(facturas);

      this.lotesService.getAllObjects('').subscribe(l => {
        this.lotesList = l;
      });
    });
  }

  get itemsDetalles() {
    return this.detallesFrm.controls['itemsDetalles'] as FormArray;
  }

  agregarItemsDetalles(): void {
    const fact = this.editForm.controls['numeroFactura'].value;
    this.detallesForm.patchValue({ factura: fact });
    this.itemsDetalles.push(this.detallesForm);
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const facturas = this.createFromForm();
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
    this.previousState();
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
