import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LacampesinaSharedModule } from 'app/shared/shared.module';
import { ProductosComponent } from './productos.component';
import { ProductosDetailComponent } from './productos-detail.component';
import { ProductosUpdateComponent } from './productos-update.component';
import { ProductosDeleteDialogComponent } from './productos-delete-dialog.component';
import { productosRoute } from './productos.route';

import {ButtonModule} from 'primeng/button';

@NgModule({
  imports: [LacampesinaSharedModule, RouterModule.forChild(productosRoute), ButtonModule],
  declarations: [ProductosComponent, ProductosDetailComponent, ProductosUpdateComponent, ProductosDeleteDialogComponent],
  entryComponents: [ProductosDeleteDialogComponent],
})
export class LacampesinaProductosModule {}
