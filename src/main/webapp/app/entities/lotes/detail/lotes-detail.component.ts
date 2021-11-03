import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILotes } from '../lotes.model';

@Component({
  selector: 'jhi-lotes-detail',
  templateUrl: './lotes-detail.component.html',
})
export class LotesDetailComponent implements OnInit {
  lotes: ILotes | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lotes }) => {
      this.lotes = lotes;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
