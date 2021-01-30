import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IPrecios, Precios } from 'app/shared/model/precios.model';
import { PreciosService } from './precios.service';

@Component({
  selector: 'jhi-precios-update',
  templateUrl: './precios-update.component.html',
})
export class PreciosUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    createdAt: [],
    fechaFin: [],
    fechaInicio: [],
    precio: [],
  });

  constructor(protected preciosService: PreciosService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ precios }) => {
      if (!precios.id) {
        const today = moment().startOf('day');
        precios.createdAt = today;
        precios.fechaFin = today;
        precios.fechaInicio = today;
      }

      this.updateForm(precios);
    });
  }

  updateForm(precios: IPrecios): void {
    this.editForm.patchValue({
      id: precios.id,
      createdAt: precios.createdAt ? precios.createdAt.format(DATE_TIME_FORMAT) : null,
      fechaFin: precios.fechaFin ? precios.fechaFin.format(DATE_TIME_FORMAT) : null,
      fechaInicio: precios.fechaInicio ? precios.fechaInicio.format(DATE_TIME_FORMAT) : null,
      precio: precios.precio,
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
      createdAt: this.editForm.get(['createdAt'])!.value ? moment(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      fechaFin: this.editForm.get(['fechaFin'])!.value ? moment(this.editForm.get(['fechaFin'])!.value, DATE_TIME_FORMAT) : undefined,
      fechaInicio: this.editForm.get(['fechaInicio'])!.value
        ? moment(this.editForm.get(['fechaInicio'])!.value, DATE_TIME_FORMAT)
        : undefined,
      precio: this.editForm.get(['precio'])!.value,
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
}
