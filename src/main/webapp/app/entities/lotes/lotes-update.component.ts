import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ILotes, Lotes } from 'app/shared/model/lotes.model';
import { LotesService } from './lotes.service';
import { IProductos } from 'app/shared/model/productos.model';
import { ProductosService } from 'app/entities/productos/productos.service';
import { IProveedores } from 'app/shared/model/proveedores.model';
import { ProveedoresService } from 'app/entities/proveedores/proveedores.service';
import { IFacturasDetalle } from 'app/shared/model/facturas-detalle.model';
import { FacturasDetalleService } from 'app/entities/facturas-detalle/facturas-detalle.service';

type SelectableEntity = IProductos | IProveedores | IFacturasDetalle;

@Component({
  selector: 'jhi-lotes-update',
  templateUrl: './lotes-update.component.html',
})
export class LotesUpdateComponent implements OnInit {
  isSaving = false;
  productos: IProductos[] = [];
  proveedores: IProveedores[] = [];
  facturasdetalles: IFacturasDetalle[] = [];
  fechaEntradaDp: any;

  editForm = this.fb.group({
    id: [],
    cantidad: [],
    fechaEntrada: [],
    lote: [],
    productoId: [],
    proveedorId: [],
    loteId: [],
  });

  constructor(
    protected lotesService: LotesService,
    protected productosService: ProductosService,
    protected proveedoresService: ProveedoresService,
    protected facturasDetalleService: FacturasDetalleService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lotes }) => {
      this.updateForm(lotes);

      this.productosService.query().subscribe((res: HttpResponse<IProductos[]>) => (this.productos = res.body || []));

      this.proveedoresService.query().subscribe((res: HttpResponse<IProveedores[]>) => (this.proveedores = res.body || []));

      this.facturasDetalleService.query().subscribe((res: HttpResponse<IFacturasDetalle[]>) => (this.facturasdetalles = res.body || []));
    });
  }

  updateForm(lotes: ILotes): void {
    this.editForm.patchValue({
      id: lotes.id,
      cantidad: lotes.cantidad,
      fechaEntrada: lotes.fechaEntrada,
      lote: lotes.lote,
      productoId: lotes.productoId,
      proveedorId: lotes.proveedorId,
      loteId: lotes.loteId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lotes = this.createFromForm();
    if (lotes.id !== undefined) {
      this.subscribeToSaveResponse(this.lotesService.update(lotes));
    } else {
      this.subscribeToSaveResponse(this.lotesService.create(lotes));
    }
  }

  private createFromForm(): ILotes {
    return {
      ...new Lotes(),
      id: this.editForm.get(['id'])!.value,
      cantidad: this.editForm.get(['cantidad'])!.value,
      fechaEntrada: this.editForm.get(['fechaEntrada'])!.value,
      lote: this.editForm.get(['lote'])!.value,
      productoId: this.editForm.get(['productoId'])!.value,
      proveedorId: this.editForm.get(['proveedorId'])!.value,
      loteId: this.editForm.get(['loteId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILotes>>): void {
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
