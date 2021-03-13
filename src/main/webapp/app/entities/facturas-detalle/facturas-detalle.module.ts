import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LacampesinaSharedModule } from 'app/shared/shared.module';
import { FacturasDetalleComponent } from './facturas-detalle.component';
import { FacturasDetalleDetailComponent } from './facturas-detalle-detail.component';
import { FacturasDetalleUpdateComponent } from './facturas-detalle-update.component';
import { FacturasDetalleDeleteDialogComponent } from './facturas-detalle-delete-dialog.component';
import { facturasDetalleRoute } from './facturas-detalle.route';

@NgModule({
  imports: [LacampesinaSharedModule, RouterModule.forChild(facturasDetalleRoute)],
  declarations: [
    FacturasDetalleComponent,
    FacturasDetalleDetailComponent,
    FacturasDetalleUpdateComponent,
    FacturasDetalleDeleteDialogComponent,
  ],
  entryComponents: [FacturasDetalleDeleteDialogComponent],
})
export class LacampesinaFacturasDetalleModule {}
