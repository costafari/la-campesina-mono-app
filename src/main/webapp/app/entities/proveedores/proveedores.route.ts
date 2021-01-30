import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IProveedores, Proveedores } from 'app/shared/model/proveedores.model';
import { ProveedoresService } from './proveedores.service';
import { ProveedoresComponent } from './proveedores.component';
import { ProveedoresDetailComponent } from './proveedores-detail.component';
import { ProveedoresUpdateComponent } from './proveedores-update.component';

@Injectable({ providedIn: 'root' })
export class ProveedoresResolve implements Resolve<IProveedores> {
  constructor(private service: ProveedoresService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProveedores> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((proveedores: HttpResponse<Proveedores>) => {
          if (proveedores.body) {
            return of(proveedores.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Proveedores());
  }
}

export const proveedoresRoute: Routes = [
  {
    path: '',
    component: ProveedoresComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.proveedores.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProveedoresDetailComponent,
    resolve: {
      proveedores: ProveedoresResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.proveedores.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProveedoresUpdateComponent,
    resolve: {
      proveedores: ProveedoresResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.proveedores.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProveedoresUpdateComponent,
    resolve: {
      proveedores: ProveedoresResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.proveedores.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
