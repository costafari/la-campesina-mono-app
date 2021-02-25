import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IProductos, Productos } from 'app/shared/model/productos.model';
import { ProductosService } from './productos.service';

@Component({
  selector: 'jhi-productos-update',
  templateUrl: './productos-update.component.html',
})
export class ProductosUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    descipcion: [],
    nombre: [],
    notas: [],
  });

  constructor(protected productosService: ProductosService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productos }) => {
      this.updateForm(productos);
    });
  }

  updateForm(productos: IProductos): void {
    this.editForm.patchValue({
      id: productos.id,
      descipcion: productos.descipcion,
      nombre: productos.nombre,
      notas: productos.notas,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productos = this.createFromForm();
    if (productos.id !== undefined) {
      this.subscribeToSaveResponse(this.productosService.update(productos));
    } else {
      this.subscribeToSaveResponse(this.productosService.create(productos));
    }
  }

  private createFromForm(): IProductos {
    return {
      ...new Productos(),
      id: this.editForm.get(['id'])!.value,
      descipcion: this.editForm.get(['descipcion'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      notas: this.editForm.get(['notas'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductos>>): void {
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
