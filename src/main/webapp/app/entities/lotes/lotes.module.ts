import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LacampesinaSharedModule } from 'app/shared/shared.module';
import { LotesComponent } from './lotes.component';
import { LotesDetailComponent } from './lotes-detail.component';
import { LotesUpdateComponent } from './lotes-update.component';
import { LotesDeleteDialogComponent } from './lotes-delete-dialog.component';
import { lotesRoute } from './lotes.route';

@NgModule({
  imports: [LacampesinaSharedModule, RouterModule.forChild(lotesRoute)],
  declarations: [LotesComponent, LotesDetailComponent, LotesUpdateComponent, LotesDeleteDialogComponent],
  entryComponents: [LotesDeleteDialogComponent],
})
export class LacampesinaLotesModule {}
