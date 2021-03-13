import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFacturasMaster } from 'app/shared/model/facturas-master.model';

@Component({
  selector: 'jhi-facturas-master-detail',
  templateUrl: './facturas-master-detail.component.html',
})
export class FacturasMasterDetailComponent implements OnInit {
  facturasMaster: IFacturasMaster | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facturasMaster }) => (this.facturasMaster = facturasMaster));
  }

  previousState(): void {
    window.history.back();
  }
}
