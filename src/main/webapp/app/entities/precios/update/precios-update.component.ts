import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPrecios, Precios } from '../precios.model';
import { PreciosService } from '../service/precios.service';

@Component({
  selector: 'jhi-precios-update',
  templateUrl: './precios-update.component.html',
})
export class PreciosUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    fechaFin: [],
    fechaInicio: [],
    precio: [],
  });

  constructor(protected preciosService: PreciosService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ precios }) => {
      this.updateForm(precios);
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrecios>>): void {
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

  protected updateForm(precios: IPrecios): void {
    this.editForm.patchValue({
      id: precios.id,
      fechaFin: precios.fechaFin,
      fechaInicio: precios.fechaInicio,
      precio: precios.precio,
    });
  }

  protected createFromForm(): IPrecios {
    return {
      ...new Precios(),
      id: this.editForm.get(['id'])!.value,
      fechaFin: this.editForm.get(['fechaFin'])!.value,
      fechaInicio: this.editForm.get(['fechaInicio'])!.value,
      precio: this.editForm.get(['precio'])!.value,
    };
  }
}
