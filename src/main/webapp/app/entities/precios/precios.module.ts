import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PreciosComponent } from './list/precios.component';
import { PreciosDetailComponent } from './detail/precios-detail.component';
import { PreciosUpdateComponent } from './update/precios-update.component';
import { PreciosDeleteDialogComponent } from './delete/precios-delete-dialog.component';
import { PreciosRoutingModule } from './route/precios-routing.module';

@NgModule({
  imports: [SharedModule, PreciosRoutingModule],
  declarations: [PreciosComponent, PreciosDetailComponent, PreciosUpdateComponent, PreciosDeleteDialogComponent],
  entryComponents: [PreciosDeleteDialogComponent],
})
export class PreciosModule {}
