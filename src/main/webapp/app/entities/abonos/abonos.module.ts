import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AbonosComponent } from './list/abonos.component';
import { AbonosDetailComponent } from './detail/abonos-detail.component';
import { AbonosUpdateComponent } from './update/abonos-update.component';
import { AbonosDeleteDialogComponent } from './delete/abonos-delete-dialog.component';
import { AbonosRoutingModule } from './route/abonos-routing.module';

@NgModule({
  imports: [SharedModule, AbonosRoutingModule],
  declarations: [AbonosComponent, AbonosDetailComponent, AbonosUpdateComponent, AbonosDeleteDialogComponent],
  entryComponents: [AbonosDeleteDialogComponent],
})
export class AbonosModule {}
