import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDetalles, Detalles } from '../detalles.model';
import { DetallesService } from '../service/detalles.service';

@Injectable({ providedIn: 'root' })
export class DetallesRoutingResolveService implements Resolve<IDetalles> {
  constructor(protected service: DetallesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDetalles> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((detalles: HttpResponse<Detalles>) => {
          if (detalles.body) {
            return of(detalles.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Detalles());
  }
}
