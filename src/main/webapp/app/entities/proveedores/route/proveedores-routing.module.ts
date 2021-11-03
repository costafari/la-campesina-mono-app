import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProveedoresComponent } from '../list/proveedores.component';
import { ProveedoresDetailComponent } from '../detail/proveedores-detail.component';
import { ProveedoresUpdateComponent } from '../update/proveedores-update.component';
import { ProveedoresRoutingResolveService } from './proveedores-routing-resolve.service';

const proveedoresRoute: Routes = [
  {
    path: '',
    component: ProveedoresComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProveedoresDetailComponent,
    resolve: {
      proveedores: ProveedoresRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProveedoresUpdateComponent,
    resolve: {
      proveedores: ProveedoresRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProveedoresUpdateComponent,
    resolve: {
      proveedores: ProveedoresRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(proveedoresRoute)],
  exports: [RouterModule],
})
export class ProveedoresRoutingModule {}
