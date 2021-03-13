import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFacturasDetalle } from 'app/shared/model/facturas-detalle.model';

@Component({
  selector: 'jhi-facturas-detalle-detail',
  templateUrl: './facturas-detalle-detail.component.html',
})
export class FacturasDetalleDetailComponent implements OnInit {
  facturasDetalle: IFacturasDetalle | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facturasDetalle }) => (this.facturasDetalle = facturasDetalle));
  }

  previousState(): void {
    window.history.back();
  }
}
