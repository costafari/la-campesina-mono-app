import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IFacturasDetalle } from 'app/shared/model/facturas-detalle.model';

type EntityResponseType = HttpResponse<IFacturasDetalle>;
type EntityArrayResponseType = HttpResponse<IFacturasDetalle[]>;

@Injectable({ providedIn: 'root' })
export class FacturasDetalleService {
  public resourceUrl = SERVER_API_URL + 'api/facturas-detalles';

  constructor(protected http: HttpClient) {}

  create(facturasDetalle: IFacturasDetalle): Observable<EntityResponseType> {
    return this.http.post<IFacturasDetalle>(this.resourceUrl, facturasDetalle, { observe: 'response' });
  }

  update(facturasDetalle: IFacturasDetalle): Observable<EntityResponseType> {
    return this.http.put<IFacturasDetalle>(this.resourceUrl, facturasDetalle, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFacturasDetalle>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFacturasDetalle[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
