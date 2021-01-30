import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LacampesinaSharedModule } from 'app/shared/shared.module';
import { ClientesComponent } from './clientes.component';
import { ClientesDetailComponent } from './clientes-detail.component';
import { ClientesUpdateComponent } from './clientes-update.component';
import { ClientesDeleteDialogComponent } from './clientes-delete-dialog.component';
import { clientesRoute } from './clientes.route';

@NgModule({
  imports: [LacampesinaSharedModule, RouterModule.forChild(clientesRoute)],
  declarations: [ClientesComponent, ClientesDetailComponent, ClientesUpdateComponent, ClientesDeleteDialogComponent],
  entryComponents: [ClientesDeleteDialogComponent],
})
export class LacampesinaClientesModule {}
