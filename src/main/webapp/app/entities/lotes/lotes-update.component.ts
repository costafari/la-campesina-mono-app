import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ILotes, Lotes } from 'app/shared/model/lotes.model';
import { LotesService } from './lotes.service';

@Component({
  selector: 'jhi-lotes-update',
  templateUrl: './lotes-update.component.html',
})
export class LotesUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    cantidad: [],
    createdAt: [],
    fechaEntrada: [],
    lote: [],
  });

  constructor(protected lotesService: LotesService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lotes }) => {
      if (!lotes.id) {
        const today = moment().startOf('day');
        lotes.createdAt = today;
        lotes.fechaEntrada = today;
      }

      this.updateForm(lotes);
    });
  }

  updateForm(lotes: ILotes): void {
    this.editForm.patchValue({
      id: lotes.id,
      cantidad: lotes.cantidad,
      createdAt: lotes.createdAt ? lotes.createdAt.format(DATE_TIME_FORMAT) : null,
      fechaEntrada: lotes.fechaEntrada ? lotes.fechaEntrada.format(DATE_TIME_FORMAT) : null,
      lote: lotes.lote,
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
      createdAt: this.editForm.get(['createdAt'])!.value ? moment(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      fechaEntrada: this.editForm.get(['fechaEntrada'])!.value
        ? moment(this.editForm.get(['fechaEntrada'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lote: this.editForm.get(['lote'])!.value,
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
}
