import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LacampesinaSharedModule } from 'app/shared/shared.module';
import { PreciosComponent } from './precios.component';
import { PreciosDetailComponent } from './precios-detail.component';
import { PreciosUpdateComponent } from './precios-update.component';
import { PreciosDeleteDialogComponent } from './precios-delete-dialog.component';
import { preciosRoute } from './precios.route';

@NgModule({
  imports: [LacampesinaSharedModule, RouterModule.forChild(preciosRoute)],
  declarations: [PreciosComponent, PreciosDetailComponent, PreciosUpdateComponent, PreciosDeleteDialogComponent],
  entryComponents: [PreciosDeleteDialogComponent],
})
export class LacampesinaPreciosModule {}
