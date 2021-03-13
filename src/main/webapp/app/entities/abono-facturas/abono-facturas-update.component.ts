import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAbonoFacturas, AbonoFacturas } from 'app/shared/model/abono-facturas.model';
import { AbonoFacturasService } from './abono-facturas.service';
import { IFacturasMaster } from 'app/shared/model/facturas-master.model';
import { FacturasMasterService } from 'app/entities/facturas-master/facturas-master.service';

@Component({
  selector: 'jhi-abono-facturas-update',
  templateUrl: './abono-facturas-update.component.html',
})
export class AbonoFacturasUpdateComponent implements OnInit {
  isSaving = false;
  facturasmasters: IFacturasMaster[] = [];

  editForm = this.fb.group({
    id: [],
    saldoAnterior: [null, [Validators.required]],
    abono: [null, [Validators.required]],
    nuevoSaldo: [],
    abonoId: [],
  });

  constructor(
    protected abonoFacturasService: AbonoFacturasService,
    protected facturasMasterService: FacturasMasterService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ abonoFacturas }) => {
      this.updateForm(abonoFacturas);

      this.facturasMasterService.query().subscribe((res: HttpResponse<IFacturasMaster[]>) => (this.facturasmasters = res.body || []));
    });
  }

  updateForm(abonoFacturas: IAbonoFacturas): void {
    this.editForm.patchValue({
      id: abonoFacturas.id,
      saldoAnterior: abonoFacturas.saldoAnterior,
      abono: abonoFacturas.abono,
      nuevoSaldo: abonoFacturas.nuevoSaldo,
      abonoId: abonoFacturas.abonoId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const abonoFacturas = this.createFromForm();
    if (abonoFacturas.id !== undefined) {
      this.subscribeToSaveResponse(this.abonoFacturasService.update(abonoFacturas));
    } else {
      this.subscribeToSaveResponse(this.abonoFacturasService.create(abonoFacturas));
    }
  }

  private createFromForm(): IAbonoFacturas {
    return {
      ...new AbonoFacturas(),
      id: this.editForm.get(['id'])!.value,
      saldoAnterior: this.editForm.get(['saldoAnterior'])!.value,
      abono: this.editForm.get(['abono'])!.value,
      nuevoSaldo: this.editForm.get(['nuevoSaldo'])!.value,
      abonoId: this.editForm.get(['abonoId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAbonoFacturas>>): void {
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

  trackById(index: number, item: IFacturasMaster): any {
    return item.id;
  }
}
