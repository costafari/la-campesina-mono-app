import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrecios } from '../precios.model';

@Component({
  selector: 'jhi-precios-detail',
  templateUrl: './precios-detail.component.html',
})
export class PreciosDetailComponent implements OnInit {
  precios: IPrecios | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ precios }) => {
      this.precios = precios;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
