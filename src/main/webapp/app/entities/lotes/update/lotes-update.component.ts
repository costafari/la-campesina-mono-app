import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILotes, Lotes } from '../lotes.model';
import { LotesService } from '../service/lotes.service';
import { IProductos } from 'app/entities/productos/productos.model';
import { ProductosService } from 'app/entities/productos/service/productos.service';
import { IPrecios } from 'app/entities/precios/precios.model';
import { PreciosService } from 'app/entities/precios/service/precios.service';

@Component({
  selector: 'jhi-lotes-update',
  templateUrl: './lotes-update.component.html',
})
export class LotesUpdateComponent implements OnInit {
  isSaving = false;

  productosSharedCollection: IProductos[] = [];
  preciosSharedCollection: IPrecios[] = [];

  editForm = this.fb.group({
    id: [],
    cantidad: [],
    fechaEntrada: [],
    lote: [],
    productos: [],
    precios: [],
  });

  constructor(
    protected lotesService: LotesService,
    protected productosService: ProductosService,
    protected preciosService: PreciosService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lotes }) => {
      this.updateForm(lotes);

      this.loadRelationshipsOptions();
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

  trackProductosById(index: number, item: IProductos): number {
    return item.id!;
  }

  trackPreciosById(index: number, item: IPrecios): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILotes>>): void {
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

  protected updateForm(lotes: ILotes): void {
    this.editForm.patchValue({
      id: lotes.id,
      cantidad: lotes.cantidad,
      fechaEntrada: lotes.fechaEntrada,
      lote: lotes.lote,
      productos: lotes.productos,
      precios: lotes.precios,
    });

    this.productosSharedCollection = this.productosService.addProductosToCollectionIfMissing(
      this.productosSharedCollection,
      lotes.productos
    );
    this.preciosSharedCollection = this.preciosService.addPreciosToCollectionIfMissing(this.preciosSharedCollection, lotes.precios);
  }

  protected loadRelationshipsOptions(): void {
    this.productosService
      .query()
      .pipe(map((res: HttpResponse<IProductos[]>) => res.body ?? []))
      .pipe(
        map((productos: IProductos[]) =>
          this.productosService.addProductosToCollectionIfMissing(productos, this.editForm.get('productos')!.value)
        )
      )
      .subscribe((productos: IProductos[]) => (this.productosSharedCollection = productos));

    this.preciosService
      .query()
      .pipe(map((res: HttpResponse<IPrecios[]>) => res.body ?? []))
      .pipe(map((precios: IPrecios[]) => this.preciosService.addPreciosToCollectionIfMissing(precios, this.editForm.get('precios')!.value)))
      .subscribe((precios: IPrecios[]) => (this.preciosSharedCollection = precios));
  }

  protected createFromForm(): ILotes {
    return {
      ...new Lotes(),
      id: this.editForm.get(['id'])!.value,
      cantidad: this.editForm.get(['cantidad'])!.value,
      fechaEntrada: this.editForm.get(['fechaEntrada'])!.value,
      lote: this.editForm.get(['lote'])!.value,
      productos: this.editForm.get(['productos'])!.value,
      precios: this.editForm.get(['precios'])!.value,
    };
  }
}
