import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IClientes, Clientes } from 'app/shared/model/clientes.model';
import { ClientesService } from './clientes.service';

@Component({
  selector: 'jhi-clientes-update',
  templateUrl: './clientes-update.component.html',
})
export class ClientesUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    activo: [],
    apellidos: [],
    createdAt: [],
    direcion: [],
    email: [],
    nombreContacto: [],
    nombreEmpresa: [],
    nombres: [],
    notas: [],
    sitioWeb: [],
    telefonoFijo: [],
    telefonoFijo2: [],
    telefonoMovil: [],
    telefonoMovil2: [],
  });

  constructor(protected clientesService: ClientesService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ clientes }) => {
      if (!clientes.id) {
        const today = moment().startOf('day');
        clientes.createdAt = today;
      }

      this.updateForm(clientes);
    });
  }

  updateForm(clientes: IClientes): void {
    this.editForm.patchValue({
      id: clientes.id,
      activo: clientes.activo,
      apellidos: clientes.apellidos,
      createdAt: clientes.createdAt ? clientes.createdAt.format(DATE_TIME_FORMAT) : null,
      direcion: clientes.direcion,
      email: clientes.email,
      nombreContacto: clientes.nombreContacto,
      nombreEmpresa: clientes.nombreEmpresa,
      nombres: clientes.nombres,
      notas: clientes.notas,
      sitioWeb: clientes.sitioWeb,
      telefonoFijo: clientes.telefonoFijo,
      telefonoFijo2: clientes.telefonoFijo2,
      telefonoMovil: clientes.telefonoMovil,
      telefonoMovil2: clientes.telefonoMovil2,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const clientes = this.createFromForm();
    if (clientes.id !== undefined) {
      this.subscribeToSaveResponse(this.clientesService.update(clientes));
    } else {
      this.subscribeToSaveResponse(this.clientesService.create(clientes));
    }
  }

  private createFromForm(): IClientes {
    return {
      ...new Clientes(),
      id: this.editForm.get(['id'])!.value,
      activo: this.editForm.get(['activo'])!.value,
      apellidos: this.editForm.get(['apellidos'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? moment(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      direcion: this.editForm.get(['direcion'])!.value,
      email: this.editForm.get(['email'])!.value,
      nombreContacto: this.editForm.get(['nombreContacto'])!.value,
      nombreEmpresa: this.editForm.get(['nombreEmpresa'])!.value,
      nombres: this.editForm.get(['nombres'])!.value,
      notas: this.editForm.get(['notas'])!.value,
      sitioWeb: this.editForm.get(['sitioWeb'])!.value,
      telefonoFijo: this.editForm.get(['telefonoFijo'])!.value,
      telefonoFijo2: this.editForm.get(['telefonoFijo2'])!.value,
      telefonoMovil: this.editForm.get(['telefonoMovil'])!.value,
      telefonoMovil2: this.editForm.get(['telefonoMovil2'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClientes>>): void {
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
