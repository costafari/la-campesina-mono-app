import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IProductos, Productos } from 'app/shared/model/productos.model';
import { ProductosService } from './productos.service';
import { ProductosComponent } from './productos.component';
import { ProductosDetailComponent } from './productos-detail.component';
import { ProductosUpdateComponent } from './productos-update.component';

@Injectable({ providedIn: 'root' })
export class ProductosResolve implements Resolve<IProductos> {
  constructor(private service: ProductosService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProductos> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((productos: HttpResponse<Productos>) => {
          if (productos.body) {
            return of(productos.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Productos());
  }
}

export const productosRoute: Routes = [
  {
    path: '',
    component: ProductosComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.productos.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductosDetailComponent,
    resolve: {
      productos: ProductosResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.productos.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductosUpdateComponent,
    resolve: {
      productos: ProductosResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.productos.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductosUpdateComponent,
    resolve: {
      productos: ProductosResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.productos.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
