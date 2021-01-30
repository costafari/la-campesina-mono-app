import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IGeneralidades, Generalidades } from 'app/shared/model/generalidades.model';
import { GeneralidadesService } from './generalidades.service';

@Component({
  selector: 'jhi-generalidades-update',
  templateUrl: './generalidades-update.component.html',
})
export class GeneralidadesUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    createdAt: [],
    fechaInicio: [],
    nombreEmpresa: [],
    nombrePropietario: [],
  });

  constructor(protected generalidadesService: GeneralidadesService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ generalidades }) => {
      if (!generalidades.id) {
        const today = moment().startOf('day');
        generalidades.createdAt = today;
        generalidades.fechaInicio = today;
      }

      this.updateForm(generalidades);
    });
  }

  updateForm(generalidades: IGeneralidades): void {
    this.editForm.patchValue({
      id: generalidades.id,
      createdAt: generalidades.createdAt ? generalidades.createdAt.format(DATE_TIME_FORMAT) : null,
      fechaInicio: generalidades.fechaInicio ? generalidades.fechaInicio.format(DATE_TIME_FORMAT) : null,
      nombreEmpresa: generalidades.nombreEmpresa,
      nombrePropietario: generalidades.nombrePropietario,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const generalidades = this.createFromForm();
    if (generalidades.id !== undefined) {
      this.subscribeToSaveResponse(this.generalidadesService.update(generalidades));
    } else {
      this.subscribeToSaveResponse(this.generalidadesService.create(generalidades));
    }
  }

  private createFromForm(): IGeneralidades {
    return {
      ...new Generalidades(),
      id: this.editForm.get(['id'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? moment(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      fechaInicio: this.editForm.get(['fechaInicio'])!.value
        ? moment(this.editForm.get(['fechaInicio'])!.value, DATE_TIME_FORMAT)
        : undefined,
      nombreEmpresa: this.editForm.get(['nombreEmpresa'])!.value,
      nombrePropietario: this.editForm.get(['nombrePropietario'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGeneralidades>>): void {
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
