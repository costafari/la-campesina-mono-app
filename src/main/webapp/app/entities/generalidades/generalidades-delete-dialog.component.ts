import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGeneralidades } from 'app/shared/model/generalidades.model';
import { GeneralidadesService } from './generalidades.service';

@Component({
  templateUrl: './generalidades-delete-dialog.component.html',
})
export class GeneralidadesDeleteDialogComponent {
  generalidades?: IGeneralidades;

  constructor(
    protected generalidadesService: GeneralidadesService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.generalidadesService.delete(id).subscribe(() => {
      this.eventManager.broadcast('generalidadesListModification');
      this.activeModal.close();
    });
  }
}
