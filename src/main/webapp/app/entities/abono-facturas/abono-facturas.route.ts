import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAbonoFacturas, AbonoFacturas } from 'app/shared/model/abono-facturas.model';
import { AbonoFacturasService } from './abono-facturas.service';
import { AbonoFacturasComponent } from './abono-facturas.component';
import { AbonoFacturasDetailComponent } from './abono-facturas-detail.component';
import { AbonoFacturasUpdateComponent } from './abono-facturas-update.component';

@Injectable({ providedIn: 'root' })
export class AbonoFacturasResolve implements Resolve<IAbonoFacturas> {
  constructor(private service: AbonoFacturasService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAbonoFacturas> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((abonoFacturas: HttpResponse<AbonoFacturas>) => {
          if (abonoFacturas.body) {
            return of(abonoFacturas.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AbonoFacturas());
  }
}

export const abonoFacturasRoute: Routes = [
  {
    path: '',
    component: AbonoFacturasComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.abonoFacturas.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AbonoFacturasDetailComponent,
    resolve: {
      abonoFacturas: AbonoFacturasResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.abonoFacturas.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AbonoFacturasUpdateComponent,
    resolve: {
      abonoFacturas: AbonoFacturasResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.abonoFacturas.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AbonoFacturasUpdateComponent,
    resolve: {
      abonoFacturas: AbonoFacturasResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.abonoFacturas.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
