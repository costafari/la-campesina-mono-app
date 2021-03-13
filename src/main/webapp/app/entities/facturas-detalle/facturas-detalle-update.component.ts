import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IFacturasDetalle, FacturasDetalle } from 'app/shared/model/facturas-detalle.model';
import { FacturasDetalleService } from './facturas-detalle.service';

@Component({
  selector: 'jhi-facturas-detalle-update',
  templateUrl: './facturas-detalle-update.component.html',
})
export class FacturasDetalleUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    cantidad: [null, [Validators.required]],
    total: [],
  });

  constructor(
    protected facturasDetalleService: FacturasDetalleService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facturasDetalle }) => {
      this.updateForm(facturasDetalle);
    });
  }

  updateForm(facturasDetalle: IFacturasDetalle): void {
    this.editForm.patchValue({
      id: facturasDetalle.id,
      cantidad: facturasDetalle.cantidad,
      total: facturasDetalle.total,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const facturasDetalle = this.createFromForm();
    if (facturasDetalle.id !== undefined) {
      this.subscribeToSaveResponse(this.facturasDetalleService.update(facturasDetalle));
    } else {
      this.subscribeToSaveResponse(this.facturasDetalleService.create(facturasDetalle));
    }
  }

  private createFromForm(): IFacturasDetalle {
    return {
      ...new FacturasDetalle(),
      id: this.editForm.get(['id'])!.value,
      cantidad: this.editForm.get(['cantidad'])!.value,
      total: this.editForm.get(['total'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFacturasDetalle>>): void {
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
}
