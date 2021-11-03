import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LotesComponent } from '../list/lotes.component';
import { LotesDetailComponent } from '../detail/lotes-detail.component';
import { LotesUpdateComponent } from '../update/lotes-update.component';
import { LotesRoutingResolveService } from './lotes-routing-resolve.service';

const lotesRoute: Routes = [
  {
    path: '',
    component: LotesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LotesDetailComponent,
    resolve: {
      lotes: LotesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LotesUpdateComponent,
    resolve: {
      lotes: LotesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LotesUpdateComponent,
    resolve: {
      lotes: LotesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(lotesRoute)],
  exports: [RouterModule],
})
export class LotesRoutingModule {}
