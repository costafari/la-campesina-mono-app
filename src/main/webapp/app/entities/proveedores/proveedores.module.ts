import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LacampesinaSharedModule } from 'app/shared/shared.module';
import { ProveedoresComponent } from './proveedores.component';
import { ProveedoresDetailComponent } from './proveedores-detail.component';
import { ProveedoresUpdateComponent } from './proveedores-update.component';
import { ProveedoresDeleteDialogComponent } from './proveedores-delete-dialog.component';
import { proveedoresRoute } from './proveedores.route';

@NgModule({
  imports: [LacampesinaSharedModule, RouterModule.forChild(proveedoresRoute)],
  declarations: [ProveedoresComponent, ProveedoresDetailComponent, ProveedoresUpdateComponent, ProveedoresDeleteDialogComponent],
  entryComponents: [ProveedoresDeleteDialogComponent],
})
export class LacampesinaProveedoresModule {}
