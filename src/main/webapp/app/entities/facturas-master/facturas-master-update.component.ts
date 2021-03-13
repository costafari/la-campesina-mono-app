import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IFacturasMaster, FacturasMaster } from 'app/shared/model/facturas-master.model';
import { FacturasMasterService } from './facturas-master.service';
import { IFacturasDetalle } from 'app/shared/model/facturas-detalle.model';
import { FacturasDetalleService } from 'app/entities/facturas-detalle/facturas-detalle.service';

@Component({
  selector: 'jhi-facturas-master-update',
  templateUrl: './facturas-master-update.component.html',
})
export class FacturasMasterUpdateComponent implements OnInit {
  isSaving = false;
  facturasdetalles: IFacturasDetalle[] = [];

  editForm = this.fb.group({
    id: [],
    numeroFactura: [null, [Validators.required]],
    fechaFactura: [],
    condicionPago: [],
    facturaMasterId: [],
  });

  constructor(
    protected facturasMasterService: FacturasMasterService,
    protected facturasDetalleService: FacturasDetalleService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facturasMaster }) => {
      if (!facturasMaster.id) {
        const today = moment().startOf('day');
        facturasMaster.fechaFactura = today;
      }

      this.updateForm(facturasMaster);

      this.facturasDetalleService.query().subscribe((res: HttpResponse<IFacturasDetalle[]>) => (this.facturasdetalles = res.body || []));
    });
  }

  updateForm(facturasMaster: IFacturasMaster): void {
    this.editForm.patchValue({
      id: facturasMaster.id,
      numeroFactura: facturasMaster.numeroFactura,
      fechaFactura: facturasMaster.fechaFactura ? facturasMaster.fechaFactura.format(DATE_TIME_FORMAT) : null,
      condicionPago: facturasMaster.condicionPago,
      facturaMasterId: facturasMaster.facturaMasterId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const facturasMaster = this.createFromForm();
    if (facturasMaster.id !== undefined) {
      this.subscribeToSaveResponse(this.facturasMasterService.update(facturasMaster));
    } else {
      this.subscribeToSaveResponse(this.facturasMasterService.create(facturasMaster));
    }
  }

  private createFromForm(): IFacturasMaster {
    return {
      ...new FacturasMaster(),
      id: this.editForm.get(['id'])!.value,
      numeroFactura: this.editForm.get(['numeroFactura'])!.value,
      fechaFactura: this.editForm.get(['fechaFactura'])!.value
        ? moment(this.editForm.get(['fechaFactura'])!.value, DATE_TIME_FORMAT)
        : undefined,
      condicionPago: this.editForm.get(['condicionPago'])!.value,
      facturaMasterId: this.editForm.get(['facturaMasterId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFacturasMaster>>): void {
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

  trackById(index: number, item: IFacturasDetalle): any {
    return item.id;
  }
}
