import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAbonos, Abonos } from '../abonos.model';
import { AbonosService } from '../service/abonos.service';
import { IFacturas } from 'app/entities/facturas/facturas.model';
import { FacturasService } from 'app/entities/facturas/service/facturas.service';

@Component({
  selector: 'jhi-abonos-update',
  templateUrl: './abonos-update.component.html',
})
export class AbonosUpdateComponent implements OnInit {
  isSaving = false;

  facturasSharedCollection: IFacturas[] = [];

  editForm = this.fb.group({
    id: [],
    saldoAnterior: [null, [Validators.required]],
    abono: [null, [Validators.required]],
    nuevoSaldo: [],
    facturas: [],
  });

  constructor(
    protected abonosService: AbonosService,
    protected facturasService: FacturasService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ abonos }) => {
      this.updateForm(abonos);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const abonos = this.createFromForm();
    if (abonos.id !== undefined) {
      this.subscribeToSaveResponse(this.abonosService.update(abonos));
    } else {
      this.subscribeToSaveResponse(this.abonosService.create(abonos));
    }
  }

  trackFacturasById(index: number, item: IFacturas): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAbonos>>): void {
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

  protected updateForm(abonos: IAbonos): void {
    this.editForm.patchValue({
      id: abonos.id,
      saldoAnterior: abonos.saldoAnterior,
      abono: abonos.abono,
      nuevoSaldo: abonos.nuevoSaldo,
      facturas: abonos.facturas,
    });

    this.facturasSharedCollection = this.facturasService.addFacturasToCollectionIfMissing(this.facturasSharedCollection, abonos.facturas);
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
  }

  protected createFromForm(): IAbonos {
    return {
      ...new Abonos(),
      id: this.editForm.get(['id'])!.value,
      saldoAnterior: this.editForm.get(['saldoAnterior'])!.value,
      abono: this.editForm.get(['abono'])!.value,
      nuevoSaldo: this.editForm.get(['nuevoSaldo'])!.value,
      facturas: this.editForm.get(['facturas'])!.value,
    };
  }
}
