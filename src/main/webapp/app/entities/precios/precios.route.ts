import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPrecios, Precios } from 'app/shared/model/precios.model';
import { PreciosService } from './precios.service';
import { PreciosComponent } from './precios.component';
import { PreciosDetailComponent } from './precios-detail.component';
import { PreciosUpdateComponent } from './precios-update.component';

@Injectable({ providedIn: 'root' })
export class PreciosResolve implements Resolve<IPrecios> {
  constructor(private service: PreciosService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrecios> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((precios: HttpResponse<Precios>) => {
          if (precios.body) {
            return of(precios.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Precios());
  }
}

export const preciosRoute: Routes = [
  {
    path: '',
    component: PreciosComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.precios.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PreciosDetailComponent,
    resolve: {
      precios: PreciosResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.precios.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PreciosUpdateComponent,
    resolve: {
      precios: PreciosResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.precios.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PreciosUpdateComponent,
    resolve: {
      precios: PreciosResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.precios.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
