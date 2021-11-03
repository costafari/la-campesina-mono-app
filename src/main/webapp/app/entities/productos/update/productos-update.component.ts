import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IProductos, Productos } from '../productos.model';
import { ProductosService } from '../service/productos.service';

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

  constructor(protected productosService: ProductosService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productos }) => {
      this.updateForm(productos);
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductos>>): void {
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

  protected updateForm(productos: IProductos): void {
    this.editForm.patchValue({
      id: productos.id,
      descipcion: productos.descipcion,
      nombre: productos.nombre,
      notas: productos.notas,
    });
  }

  protected createFromForm(): IProductos {
    return {
      ...new Productos(),
      id: this.editForm.get(['id'])!.value,
      descipcion: this.editForm.get(['descipcion'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      notas: this.editForm.get(['notas'])!.value,
    };
  }
}
