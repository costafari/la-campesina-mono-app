import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProveedores } from '../proveedores.model';

@Component({
  selector: 'jhi-proveedores-detail',
  templateUrl: './proveedores-detail.component.html',
})
export class ProveedoresDetailComponent implements OnInit {
  proveedores: IProveedores | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proveedores }) => {
      this.proveedores = proveedores;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
