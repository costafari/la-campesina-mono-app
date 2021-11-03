import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAbonos } from '../abonos.model';

@Component({
  selector: 'jhi-abonos-detail',
  templateUrl: './abonos-detail.component.html',
})
export class AbonosDetailComponent implements OnInit {
  abonos: IAbonos | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ abonos }) => {
      this.abonos = abonos;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
