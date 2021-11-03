import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrecios, Precios } from '../precios.model';
import { PreciosService } from '../service/precios.service';

@Injectable({ providedIn: 'root' })
export class PreciosRoutingResolveService implements Resolve<IPrecios> {
  constructor(protected service: PreciosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrecios> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((precios: HttpResponse<Precios>) => {
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
