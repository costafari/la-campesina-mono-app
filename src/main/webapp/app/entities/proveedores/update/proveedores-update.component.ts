import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProveedores, Proveedores } from '../proveedores.model';
import { ProveedoresService } from '../service/proveedores.service';
import { ILotes } from 'app/entities/lotes/lotes.model';
import { LotesService } from 'app/entities/lotes/service/lotes.service';

@Component({
  selector: 'jhi-proveedores-update',
  templateUrl: './proveedores-update.component.html',
})
export class ProveedoresUpdateComponent implements OnInit {
  isSaving = false;

  lotesCollection: ILotes[] = [];

  editForm = this.fb.group({
    id: [],
    direccion: [],
    nombreContacto: [],
    nombreEmpresa: [],
    notas: [],
    sitioWeb: [],
    telefonoFijo: [],
    telefonoFijo2: [],
    telefonoMovil: [],
    telefonoMovil2: [],
    lotes: [],
  });

  constructor(
    protected proveedoresService: ProveedoresService,
    protected lotesService: LotesService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proveedores }) => {
      this.updateForm(proveedores);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const proveedores = this.createFromForm();
    if (proveedores.id !== undefined) {
      this.subscribeToSaveResponse(this.proveedoresService.update(proveedores));
    } else {
      this.subscribeToSaveResponse(this.proveedoresService.create(proveedores));
    }
  }

  trackLotesById(index: number, item: ILotes): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProveedores>>): void {
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

  protected updateForm(proveedores: IProveedores): void {
    this.editForm.patchValue({
      id: proveedores.id,
      direccion: proveedores.direccion,
      nombreContacto: proveedores.nombreContacto,
      nombreEmpresa: proveedores.nombreEmpresa,
      notas: proveedores.notas,
      sitioWeb: proveedores.sitioWeb,
      telefonoFijo: proveedores.telefonoFijo,
      telefonoFijo2: proveedores.telefonoFijo2,
      telefonoMovil: proveedores.telefonoMovil,
      telefonoMovil2: proveedores.telefonoMovil2,
      lotes: proveedores.lotes,
    });

    this.lotesCollection = this.lotesService.addLotesToCollectionIfMissing(this.lotesCollection, proveedores.lotes);
  }

  protected loadRelationshipsOptions(): void {
    this.lotesService
      .query({ filter: 'proveedores-is-null' })
      .pipe(map((res: HttpResponse<ILotes[]>) => res.body ?? []))
      .pipe(map((lotes: ILotes[]) => this.lotesService.addLotesToCollectionIfMissing(lotes, this.editForm.get('lotes')!.value)))
      .subscribe((lotes: ILotes[]) => (this.lotesCollection = lotes));
  }

  protected createFromForm(): IProveedores {
    return {
      ...new Proveedores(),
      id: this.editForm.get(['id'])!.value,
      direccion: this.editForm.get(['direccion'])!.value,
      nombreContacto: this.editForm.get(['nombreContacto'])!.value,
      nombreEmpresa: this.editForm.get(['nombreEmpresa'])!.value,
      notas: this.editForm.get(['notas'])!.value,
      sitioWeb: this.editForm.get(['sitioWeb'])!.value,
      telefonoFijo: this.editForm.get(['telefonoFijo'])!.value,
      telefonoFijo2: this.editForm.get(['telefonoFijo2'])!.value,
      telefonoMovil: this.editForm.get(['telefonoMovil'])!.value,
      telefonoMovil2: this.editForm.get(['telefonoMovil2'])!.value,
      lotes: this.editForm.get(['lotes'])!.value,
    };
  }
}
