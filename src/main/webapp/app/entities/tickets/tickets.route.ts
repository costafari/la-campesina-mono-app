import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITickets, Tickets } from 'app/shared/model/tickets.model';
import { TicketsService } from './tickets.service';
import { TicketsComponent } from './tickets.component';
import { TicketsDetailComponent } from './tickets-detail.component';
import { TicketsUpdateComponent } from './tickets-update.component';

@Injectable({ providedIn: 'root' })
export class TicketsResolve implements Resolve<ITickets> {
  constructor(private service: TicketsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITickets> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((tickets: HttpResponse<Tickets>) => {
          if (tickets.body) {
            return of(tickets.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Tickets());
  }
}

export const ticketsRoute: Routes = [
  {
    path: '',
    component: TicketsComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.tickets.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TicketsDetailComponent,
    resolve: {
      tickets: TicketsResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.tickets.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TicketsUpdateComponent,
    resolve: {
      tickets: TicketsResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.tickets.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TicketsUpdateComponent,
    resolve: {
      tickets: TicketsResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'lacampesinaApp.tickets.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
