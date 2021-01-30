import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IProveedores, Proveedores } from 'app/shared/model/proveedores.model';
import { ProveedoresService } from './proveedores.service';

@Component({
  selector: 'jhi-proveedores-update',
  templateUrl: './proveedores-update.component.html',
})
export class ProveedoresUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    createdAt: [],
    direccion: [],
    nombreContacto: [],
    nombreEmpresa: [],
    notas: [],
    sitioWeb: [],
    telefonoFijo: [],
    telefonoFijo2: [],
    telefonoMovil: [],
    telefonoMovil2: [],
  });

  constructor(protected proveedoresService: ProveedoresService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proveedores }) => {
      if (!proveedores.id) {
        const today = moment().startOf('day');
        proveedores.createdAt = today;
      }

      this.updateForm(proveedores);
    });
  }

  updateForm(proveedores: IProveedores): void {
    this.editForm.patchValue({
      id: proveedores.id,
      createdAt: proveedores.createdAt ? proveedores.createdAt.format(DATE_TIME_FORMAT) : null,
      direccion: proveedores.direccion,
      nombreContacto: proveedores.nombreContacto,
      nombreEmpresa: proveedores.nombreEmpresa,
      notas: proveedores.notas,
      sitioWeb: proveedores.sitioWeb,
      telefonoFijo: proveedores.telefonoFijo,
      telefonoFijo2: proveedores.telefonoFijo2,
      telefonoMovil: proveedores.telefonoMovil,
      telefonoMovil2: proveedores.telefonoMovil2,
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

  private createFromForm(): IProveedores {
    return {
      ...new Proveedores(),
      id: this.editForm.get(['id'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? moment(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      direccion: this.editForm.get(['direccion'])!.value,
      nombreContacto: this.editForm.get(['nombreContacto'])!.value,
      nombreEmpresa: this.editForm.get(['nombreEmpresa'])!.value,
      notas: this.editForm.get(['notas'])!.value,
      sitioWeb: this.editForm.get(['sitioWeb'])!.value,
      telefonoFijo: this.editForm.get(['telefonoFijo'])!.value,
      telefonoFijo2: this.editForm.get(['telefonoFijo2'])!.value,
      telefonoMovil: this.editForm.get(['telefonoMovil'])!.value,
      telefonoMovil2: this.editForm.get(['telefonoMovil2'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProveedores>>): void {
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
