import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LacampesinaSharedModule } from 'app/shared/shared.module';
import { GeneralidadesComponent } from './generalidades.component';
import { GeneralidadesDetailComponent } from './generalidades-detail.component';
import { GeneralidadesUpdateComponent } from './generalidades-update.component';
import { GeneralidadesDeleteDialogComponent } from './generalidades-delete-dialog.component';
import { generalidadesRoute } from './generalidades.route';

@NgModule({
  imports: [LacampesinaSharedModule, RouterModule.forChild(generalidadesRoute)],
  declarations: [GeneralidadesComponent, GeneralidadesDetailComponent, GeneralidadesUpdateComponent, GeneralidadesDeleteDialogComponent],
  entryComponents: [GeneralidadesDeleteDialogComponent],
})
export class LacampesinaGeneralidadesModule {}
