import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LotesComponent } from './list/lotes.component';
import { LotesDetailComponent } from './detail/lotes-detail.component';
import { LotesUpdateComponent } from './update/lotes-update.component';
import { LotesDeleteDialogComponent } from './delete/lotes-delete-dialog.component';
import { LotesRoutingModule } from './route/lotes-routing.module';

@NgModule({
  imports: [SharedModule, LotesRoutingModule],
  declarations: [LotesComponent, LotesDetailComponent, LotesUpdateComponent, LotesDeleteDialogComponent],
  entryComponents: [LotesDeleteDialogComponent],
})
export class LotesModule {}
