import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILotes, Lotes } from '../lotes.model';
import { LotesService } from '../service/lotes.service';

@Injectable({ providedIn: 'root' })
export class LotesRoutingResolveService implements Resolve<ILotes> {
  constructor(protected service: LotesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILotes> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((lotes: HttpResponse<Lotes>) => {
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
