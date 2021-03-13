import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFacturasMaster, FacturasMaster } from 'app/shared/model/facturas-master.model';
import { FacturasMasterService } from './facturas-master.service';
import { FacturasMasterComponent } from './facturas-master.component';
import { FacturasMasterDetailComponent } from './facturas-master-detail.component';
import { FacturasMasterUpdateComponent } from './facturas-master-update.component';

@Injectable({ providedIn: 'root' })
export class FacturasMasterResolve implements Resolve<IFacturasMaster> {
  constructor(private service: FacturasMasterService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFacturasMaster> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((facturasMaster: HttpResponse<FacturasMaster>) => {
          if (facturasMaster.body) {
            return of(facturasMaster.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FacturasMaster());
  }
}

export const facturasMasterRoute: Routes = [
  {
    path: '',
    component: FacturasMasterComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.facturasMaster.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FacturasMasterDetailComponent,
    resolve: {
      facturasMaster: FacturasMasterResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.facturasMaster.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FacturasMasterUpdateComponent,
    resolve: {
      facturasMaster: FacturasMasterResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.facturasMaster.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FacturasMasterUpdateComponent,
    resolve: {
      facturasMaster: FacturasMasterResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.facturasMaster.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
