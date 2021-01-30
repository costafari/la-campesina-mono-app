import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ILotes, Lotes } from 'app/shared/model/lotes.model';
import { LotesService } from './lotes.service';
import { LotesComponent } from './lotes.component';
import { LotesDetailComponent } from './lotes-detail.component';
import { LotesUpdateComponent } from './lotes-update.component';

@Injectable({ providedIn: 'root' })
export class LotesResolve implements Resolve<ILotes> {
  constructor(private service: LotesService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILotes> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((lotes: HttpResponse<Lotes>) => {
          if (lotes.body) {
            return of(lotes.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Lotes());
  }
}

export const lotesRoute: Routes = [
  {
    path: '',
    component: LotesComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.lotes.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LotesDetailComponent,
    resolve: {
      lotes: LotesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.lotes.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LotesUpdateComponent,
    resolve: {
      lotes: LotesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.lotes.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LotesUpdateComponent,
    resolve: {
      lotes: LotesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.lotes.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
