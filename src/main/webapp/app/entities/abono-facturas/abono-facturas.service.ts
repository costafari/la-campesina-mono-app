import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAbonoFacturas } from 'app/shared/model/abono-facturas.model';

type EntityResponseType = HttpResponse<IAbonoFacturas>;
type EntityArrayResponseType = HttpResponse<IAbonoFacturas[]>;

@Injectable({ providedIn: 'root' })
export class AbonoFacturasService {
  public resourceUrl = SERVER_API_URL + 'api/abono-facturas';

  constructor(protected http: HttpClient) {}

  create(abonoFacturas: IAbonoFacturas): Observable<EntityResponseType> {
    return this.http.post<IAbonoFacturas>(this.resourceUrl, abonoFacturas, { observe: 'response' });
  }

  update(abonoFacturas: IAbonoFacturas): Observable<EntityResponseType> {
    return this.http.put<IAbonoFacturas>(this.resourceUrl, abonoFacturas, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAbonoFacturas>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAbonoFacturas[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
