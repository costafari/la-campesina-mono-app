import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PreciosComponent } from '../list/precios.component';
import { PreciosDetailComponent } from '../detail/precios-detail.component';
import { PreciosUpdateComponent } from '../update/precios-update.component';
import { PreciosRoutingResolveService } from './precios-routing-resolve.service';

const preciosRoute: Routes = [
  {
    path: '',
    component: PreciosComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PreciosDetailComponent,
    resolve: {
      precios: PreciosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PreciosUpdateComponent,
    resolve: {
      precios: PreciosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PreciosUpdateComponent,
    resolve: {
      precios: PreciosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(preciosRoute)],
  exports: [RouterModule],
})
export class PreciosRoutingModule {}
