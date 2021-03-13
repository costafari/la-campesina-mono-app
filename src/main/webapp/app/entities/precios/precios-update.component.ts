import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPrecios, Precios } from 'app/shared/model/precios.model';
import { PreciosService } from './precios.service';
import { ILotes } from 'app/shared/model/lotes.model';
import { LotesService } from 'app/entities/lotes/lotes.service';
import { IClientes } from 'app/shared/model/clientes.model';
import { ClientesService } from 'app/entities/clientes/clientes.service';
import { IFacturasDetalle } from 'app/shared/model/facturas-detalle.model';
import { FacturasDetalleService } from 'app/entities/facturas-detalle/facturas-detalle.service';

type SelectableEntity = ILotes | IClientes | IFacturasDetalle;

@Component({
  selector: 'jhi-precios-update',
  templateUrl: './precios-update.component.html',
})
export class PreciosUpdateComponent implements OnInit {
  isSaving = false;
  lotes: ILotes[] = [];
  clientes: IClientes[] = [];
  facturasdetalles: IFacturasDetalle[] = [];
  fechaFinDp: any;
  fechaInicioDp: any;

  editForm = this.fb.group({
    id: [],
    fechaFin: [],
    fechaInicio: [],
    precio: [],
    loteId: [],
    clienteId: [],
    precioId: [],
  });

  constructor(
    protected preciosService: PreciosService,
    protected lotesService: LotesService,
    protected clientesService: ClientesService,
    protected facturasDetalleService: FacturasDetalleService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ precios }) => {
      this.updateForm(precios);

      this.lotesService.query().subscribe((res: HttpResponse<ILotes[]>) => (this.lotes = res.body || []));

      this.clientesService.query().subscribe((res: HttpResponse<IClientes[]>) => (this.clientes = res.body || []));

      this.facturasDetalleService.query().subscribe((res: HttpResponse<IFacturasDetalle[]>) => (this.facturasdetalles = res.body || []));
    });
  }

  updateForm(precios: IPrecios): void {
    this.editForm.patchValue({
      id: precios.id,
      fechaFin: precios.fechaFin,
      fechaInicio: precios.fechaInicio,
      precio: precios.precio,
      loteId: precios.loteId,
      clienteId: precios.clienteId,
      precioId: precios.precioId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const precios = this.createFromForm();
    if (precios.id !== undefined) {
      this.subscribeToSaveResponse(this.preciosService.update(precios));
    } else {
      this.subscribeToSaveResponse(this.preciosService.create(precios));
    }
  }

  private createFromForm(): IPrecios {
    return {
      ...new Precios(),
      id: this.editForm.get(['id'])!.value,
      fechaFin: this.editForm.get(['fechaFin'])!.value,
      fechaInicio: this.editForm.get(['fechaInicio'])!.value,
      precio: this.editForm.get(['precio'])!.value,
      loteId: this.editForm.get(['loteId'])!.value,
      clienteId: this.editForm.get(['clienteId'])!.value,
      precioId: this.editForm.get(['precioId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrecios>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
