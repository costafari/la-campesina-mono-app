import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DetallesComponent } from './list/detalles.component';
import { DetallesDetailComponent } from './detail/detalles-detail.component';
import { DetallesUpdateComponent } from './update/detalles-update.component';
import { DetallesDeleteDialogComponent } from './delete/detalles-delete-dialog.component';
import { DetallesRoutingModule } from './route/detalles-routing.module';

@NgModule({
  imports: [SharedModule, DetallesRoutingModule],
  declarations: [DetallesComponent, DetallesDetailComponent, DetallesUpdateComponent, DetallesDeleteDialogComponent],
  entryComponents: [DetallesDeleteDialogComponent],
})
export class DetallesModule {}
