import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAbonos, Abonos } from '../abonos.model';
import { AbonosService } from '../service/abonos.service';

@Injectable({ providedIn: 'root' })
export class AbonosRoutingResolveService implements Resolve<IAbonos> {
  constructor(protected service: AbonosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAbonos> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((abonos: HttpResponse<Abonos>) => {
          if (abonos.body) {
            return of(abonos.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Abonos());
  }
}
