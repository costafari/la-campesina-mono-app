import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LacampesinaSharedModule } from 'app/shared/shared.module';
import { AbonoFacturasComponent } from './abono-facturas.component';
import { AbonoFacturasDetailComponent } from './abono-facturas-detail.component';
import { AbonoFacturasUpdateComponent } from './abono-facturas-update.component';
import { AbonoFacturasDeleteDialogComponent } from './abono-facturas-delete-dialog.component';
import { abonoFacturasRoute } from './abono-facturas.route';

@NgModule({
  imports: [LacampesinaSharedModule, RouterModule.forChild(abonoFacturasRoute)],
  declarations: [AbonoFacturasComponent, AbonoFacturasDetailComponent, AbonoFacturasUpdateComponent, AbonoFacturasDeleteDialogComponent],
  entryComponents: [AbonoFacturasDeleteDialogComponent],
})
export class LacampesinaAbonoFacturasModule {}
