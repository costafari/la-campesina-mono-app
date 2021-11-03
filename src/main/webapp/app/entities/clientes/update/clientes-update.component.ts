import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IClientes, Clientes } from '../clientes.model';
import { ClientesService } from '../service/clientes.service';

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

  constructor(protected clientesService: ClientesService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ clientes }) => {
      this.updateForm(clientes);
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClientes>>): void {
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

  protected updateForm(clientes: IClientes): void {
    this.editForm.patchValue({
      id: clientes.id,
      activo: clientes.activo,
      apellidos: clientes.apellidos,
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

  protected createFromForm(): IClientes {
    return {
      ...new Clientes(),
      id: this.editForm.get(['id'])!.value,
      activo: this.editForm.get(['activo'])!.value,
      apellidos: this.editForm.get(['apellidos'])!.value,
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
}
