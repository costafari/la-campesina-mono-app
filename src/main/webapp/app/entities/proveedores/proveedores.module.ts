import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProveedoresComponent } from './list/proveedores.component';
import { ProveedoresDetailComponent } from './detail/proveedores-detail.component';
import { ProveedoresUpdateComponent } from './update/proveedores-update.component';
import { ProveedoresDeleteDialogComponent } from './delete/proveedores-delete-dialog.component';
import { ProveedoresRoutingModule } from './route/proveedores-routing.module';

@NgModule({
  imports: [SharedModule, ProveedoresRoutingModule],
  declarations: [ProveedoresComponent, ProveedoresDetailComponent, ProveedoresUpdateComponent, ProveedoresDeleteDialogComponent],
  entryComponents: [ProveedoresDeleteDialogComponent],
})
export class ProveedoresModule {}
