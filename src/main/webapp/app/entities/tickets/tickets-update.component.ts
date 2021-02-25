import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITickets, Tickets } from 'app/shared/model/tickets.model';
import { TicketsService } from './tickets.service';
import { IPrecios } from 'app/shared/model/precios.model';
import { PreciosService } from 'app/entities/precios/precios.service';

@Component({
  selector: 'jhi-tickets-update',
  templateUrl: './tickets-update.component.html',
})
export class TicketsUpdateComponent implements OnInit {
  isSaving = false;
  precios: IPrecios[] = [];
  fechaExpedicionDp: any;

  editForm = this.fb.group({
    id: [],
    cantidad: [],
    fechaExpedicion: [],
    total: [],
    precioId: [],
  });

  constructor(
    protected ticketsService: TicketsService,
    protected preciosService: PreciosService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tickets }) => {
      this.updateForm(tickets);

      this.preciosService.query().subscribe((res: HttpResponse<IPrecios[]>) => (this.precios = res.body || []));
    });
  }

  updateForm(tickets: ITickets): void {
    this.editForm.patchValue({
      id: tickets.id,
      cantidad: tickets.cantidad,
      fechaExpedicion: tickets.fechaExpedicion,
      total: tickets.total,
      precioId: tickets.precioId,
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
      fechaExpedicion: this.editForm.get(['fechaExpedicion'])!.value,
      total: this.editForm.get(['total'])!.value,
      precioId: this.editForm.get(['precioId'])!.value,
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

  trackById(index: number, item: IPrecios): any {
    return item.id;
  }
}
