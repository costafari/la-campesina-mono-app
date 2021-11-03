import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DetallesComponent } from '../list/detalles.component';
import { DetallesDetailComponent } from '../detail/detalles-detail.component';
import { DetallesUpdateComponent } from '../update/detalles-update.component';
import { DetallesRoutingResolveService } from './detalles-routing-resolve.service';

const detallesRoute: Routes = [
  {
    path: '',
    component: DetallesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DetallesDetailComponent,
    resolve: {
      detalles: DetallesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DetallesUpdateComponent,
    resolve: {
      detalles: DetallesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DetallesUpdateComponent,
    resolve: {
      detalles: DetallesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(detallesRoute)],
  exports: [RouterModule],
})
export class DetallesRoutingModule {}
