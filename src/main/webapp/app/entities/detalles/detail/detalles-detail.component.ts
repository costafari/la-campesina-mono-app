import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDetalles } from '../detalles.model';

@Component({
  selector: 'jhi-detalles-detail',
  templateUrl: './detalles-detail.component.html',
})
export class DetallesDetailComponent implements OnInit {
  detalles: IDetalles | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detalles }) => {
      this.detalles = detalles;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
