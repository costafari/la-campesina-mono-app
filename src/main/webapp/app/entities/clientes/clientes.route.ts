import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IClientes, Clientes } from 'app/shared/model/clientes.model';
import { ClientesService } from './clientes.service';
import { ClientesComponent } from './clientes.component';
import { ClientesDetailComponent } from './clientes-detail.component';
import { ClientesUpdateComponent } from './clientes-update.component';

@Injectable({ providedIn: 'root' })
export class ClientesResolve implements Resolve<IClientes> {
  constructor(private service: ClientesService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IClientes> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((clientes: HttpResponse<Clientes>) => {
          if (clientes.body) {
            return of(clientes.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Clientes());
  }
}

export const clientesRoute: Routes = [
  {
    path: '',
    component: ClientesComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.clientes.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClientesDetailComponent,
    resolve: {
      clientes: ClientesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.clientes.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClientesUpdateComponent,
    resolve: {
      clientes: ClientesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.clientes.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClientesUpdateComponent,
    resolve: {
      clientes: ClientesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.clientes.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
