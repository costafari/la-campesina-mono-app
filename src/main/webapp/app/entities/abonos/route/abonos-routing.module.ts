import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AbonosComponent } from '../list/abonos.component';
import { AbonosDetailComponent } from '../detail/abonos-detail.component';
import { AbonosUpdateComponent } from '../update/abonos-update.component';
import { AbonosRoutingResolveService } from './abonos-routing-resolve.service';

const abonosRoute: Routes = [
  {
    path: '',
    component: AbonosComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AbonosDetailComponent,
    resolve: {
      abonos: AbonosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AbonosUpdateComponent,
    resolve: {
      abonos: AbonosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AbonosUpdateComponent,
    resolve: {
      abonos: AbonosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(abonosRoute)],
  exports: [RouterModule],
})
export class AbonosRoutingModule {}
