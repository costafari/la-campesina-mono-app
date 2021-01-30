import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGeneralidades } from 'app/shared/model/generalidades.model';

@Component({
  selector: 'jhi-generalidades-detail',
  templateUrl: './generalidades-detail.component.html',
})
export class GeneralidadesDetailComponent implements OnInit {
  generalidades: IGeneralidades | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ generalidades }) => (this.generalidades = generalidades));
  }

  previousState(): void {
    window.history.back();
  }
}
