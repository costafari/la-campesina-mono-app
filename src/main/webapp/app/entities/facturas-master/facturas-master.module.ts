import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LacampesinaSharedModule } from 'app/shared/shared.module';
import { FacturasMasterComponent } from './facturas-master.component';
import { FacturasMasterDetailComponent } from './facturas-master-detail.component';
import { FacturasMasterUpdateComponent } from './facturas-master-update.component';
import { FacturasMasterDeleteDialogComponent } from './facturas-master-delete-dialog.component';
import { facturasMasterRoute } from './facturas-master.route';

@NgModule({
  imports: [LacampesinaSharedModule, RouterModule.forChild(facturasMasterRoute)],
  declarations: [
    FacturasMasterComponent,
    FacturasMasterDetailComponent,
    FacturasMasterUpdateComponent,
    FacturasMasterDeleteDialogComponent,
  ],
  entryComponents: [FacturasMasterDeleteDialogComponent],
})
export class LacampesinaFacturasMasterModule {}
