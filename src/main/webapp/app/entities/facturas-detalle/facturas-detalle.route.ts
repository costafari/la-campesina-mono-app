import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFacturasDetalle, FacturasDetalle } from 'app/shared/model/facturas-detalle.model';
import { FacturasDetalleService } from './facturas-detalle.service';
import { FacturasDetalleComponent } from './facturas-detalle.component';
import { FacturasDetalleDetailComponent } from './facturas-detalle-detail.component';
import { FacturasDetalleUpdateComponent } from './facturas-detalle-update.component';

@Injectable({ providedIn: 'root' })
export class FacturasDetalleResolve implements Resolve<IFacturasDetalle> {
  constructor(private service: FacturasDetalleService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFacturasDetalle> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((facturasDetalle: HttpResponse<FacturasDetalle>) => {
          if (facturasDetalle.body) {
            return of(facturasDetalle.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FacturasDetalle());
  }
}

export const facturasDetalleRoute: Routes = [
  {
    path: '',
    component: FacturasDetalleComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.facturasDetalle.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FacturasDetalleDetailComponent,
    resolve: {
      facturasDetalle: FacturasDetalleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.facturasDetalle.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FacturasDetalleUpdateComponent,
    resolve: {
      facturasDetalle: FacturasDetalleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.facturasDetalle.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FacturasDetalleUpdateComponent,
    resolve: {
      facturasDetalle: FacturasDetalleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.facturasDetalle.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
