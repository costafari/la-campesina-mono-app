import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAbonoFacturas } from 'app/shared/model/abono-facturas.model';

@Component({
  selector: 'jhi-abono-facturas-detail',
  templateUrl: './abono-facturas-detail.component.html',
})
export class AbonoFacturasDetailComponent implements OnInit {
  abonoFacturas: IAbonoFacturas | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ abonoFacturas }) => (this.abonoFacturas = abonoFacturas));
  }

  previousState(): void {
    window.history.back();
  }
}
