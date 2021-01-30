import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IGeneralidades, Generalidades } from 'app/shared/model/generalidades.model';
import { GeneralidadesService } from './generalidades.service';
import { GeneralidadesComponent } from './generalidades.component';
import { GeneralidadesDetailComponent } from './generalidades-detail.component';
import { GeneralidadesUpdateComponent } from './generalidades-update.component';

@Injectable({ providedIn: 'root' })
export class GeneralidadesResolve implements Resolve<IGeneralidades> {
  constructor(private service: GeneralidadesService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGeneralidades> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((generalidades: HttpResponse<Generalidades>) => {
          if (generalidades.body) {
            return of(generalidades.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Generalidades());
  }
}

export const generalidadesRoute: Routes = [
  {
    path: '',
    component: GeneralidadesComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.generalidades.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GeneralidadesDetailComponent,
    resolve: {
      generalidades: GeneralidadesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.generalidades.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GeneralidadesUpdateComponent,
    resolve: {
      generalidades: GeneralidadesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.generalidades.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GeneralidadesUpdateComponent,
    resolve: {
      generalidades: GeneralidadesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.generalidades.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
