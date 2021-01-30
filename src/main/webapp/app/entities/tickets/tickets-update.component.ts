import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ITickets, Tickets } from 'app/shared/model/tickets.model';
import { TicketsService } from './tickets.service';

@Component({
  selector: 'jhi-tickets-update',
  templateUrl: './tickets-update.component.html',
})
export class TicketsUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    cantidad: [],
    createdAt: [],
    fechaExpedicion: [],
    total: [],
  });

  constructor(protected ticketsService: TicketsService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tickets }) => {
      if (!tickets.id) {
        const today = moment().startOf('day');
        tickets.createdAt = today;
        tickets.fechaExpedicion = today;
      }

      this.updateForm(tickets);
    });
  }

  updateForm(tickets: ITickets): void {
    this.editForm.patchValue({
      id: tickets.id,
      cantidad: tickets.cantidad,
      createdAt: tickets.createdAt ? tickets.createdAt.format(DATE_TIME_FORMAT) : null,
      fechaExpedicion: tickets.fechaExpedicion ? tickets.fechaExpedicion.format(DATE_TIME_FORMAT) : null,
      total: tickets.total,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tickets = this.createFromForm();
    if (tickets.id !== undefined) {
      this.subscribeToSaveResponse(this.ticketsService.update(tickets));
    } else {
      this.subscribeToSaveResponse(this.ticketsService.create(tickets));
    }
  }

  private createFromForm(): ITickets {
    return {
      ...new Tickets(),
      id: this.editForm.get(['id'])!.value,
      cantidad: this.editForm.get(['cantidad'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? moment(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      fechaExpedicion: this.editForm.get(['fechaExpedicion'])!.value
        ? moment(this.editForm.get(['fechaExpedicion'])!.value, DATE_TIME_FORMAT)
        : undefined,
      total: this.editForm.get(['total'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITickets>>): void {
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
