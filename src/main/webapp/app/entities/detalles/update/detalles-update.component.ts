import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDetalles, Detalles } from '../detalles.model';
import { DetallesService } from '../service/detalles.service';
import { IFacturas } from 'app/entities/facturas/facturas.model';
import { FacturasService } from 'app/entities/facturas/service/facturas.service';
import { ILotes } from 'app/entities/lotes/lotes.model';
import { LotesService } from 'app/entities/lotes/service/lotes.service';

@Component({
  selector: 'jhi-detalles-update',
  templateUrl: './detalles-update.component.html',
})
export class DetallesUpdateComponent implements OnInit {
  isSaving = false;

  facturasSharedCollection: IFacturas[] = [];
  lotesSharedCollection: ILotes[] = [];

  editForm = this.fb.group({
    id: [],
    cantidad: [null, [Validators.required]],
    total: [],
    facturas: [],
    lotes: [],
  });

  constructor(
    protected detallesService: DetallesService,
    protected facturasService: FacturasService,
    protected lotesService: LotesService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detalles }) => {
      this.updateForm(detalles);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const detalles = this.createFromForm();
    if (detalles.id !== undefined) {
      this.subscribeToSaveResponse(this.detallesService.update(detalles));
    } else {
      this.subscribeToSaveResponse(this.detallesService.create(detalles));
    }
  }

  trackFacturasById(index: number, item: IFacturas): number {
    return item.id!;
  }

  trackLotesById(index: number, item: ILotes): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDetalles>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    // this.previousState();
    let routeFactura: IFacturas;
    routeFactura = this.editForm.get(['facturas'])!.value;
    this.router.navigate(['/facturas', routeFactura.id, 'edit']);
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(detalles: IDetalles): void {
    this.editForm.patchValue({
      id: detalles.id,
      cantidad: detalles.cantidad,
      total: detalles.total,
      facturas: detalles.facturas,
      lotes: detalles.lotes,
    });

    this.facturasSharedCollection = this.facturasService.addFacturasToCollectionIfMissing(this.facturasSharedCollection, detalles.facturas);
    this.facturasSharedCollection[this.facturasSharedCollection.length - 1];
    this.lotesSharedCollection = this.lotesService.addLotesToCollectionIfMissing(this.lotesSharedCollection, detalles.lotes);
  }

  protected loadRelationshipsOptions(): void {
    this.facturasService
      .query()
      .pipe(map((res: HttpResponse<IFacturas[]>) => res.body ?? []))
      .pipe(
        map((facturas: IFacturas[]) =>
          this.facturasService.addFacturasToCollectionIfMissing(facturas, this.editForm.get('facturas')!.value)
        )
      )
      .subscribe((facturas: IFacturas[]) => (this.facturasSharedCollection = facturas));

    this.lotesService
      .query()
      .pipe(map((res: HttpResponse<ILotes[]>) => res.body ?? []))
      .pipe(map((lotes: ILotes[]) => this.lotesService.addLotesToCollectionIfMissing(lotes, this.editForm.get('lotes')!.value)))
      .subscribe((lotes: ILotes[]) => (this.lotesSharedCollection = lotes));
  }

  protected createFromForm(): IDetalles {
    return {
      ...new Detalles(),
      id: this.editForm.get(['id'])!.value,
      cantidad: this.editForm.get(['cantidad'])!.value,
      total: this.editForm.get(['total'])!.value,
      facturas: this.editForm.get(['facturas'])!.value,
      lotes: this.editForm.get(['lotes'])!.value,
    };
  }
}
