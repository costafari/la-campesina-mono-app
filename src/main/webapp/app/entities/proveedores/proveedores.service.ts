import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IProveedores } from 'app/shared/model/proveedores.model';

type EntityResponseType = HttpResponse<IProveedores>;
type EntityArrayResponseType = HttpResponse<IProveedores[]>;

@Injectable({ providedIn: 'root' })
export class ProveedoresService {
  public resourceUrl = SERVER_API_URL + 'api/proveedores';

  constructor(protected http: HttpClient) {}

  create(proveedores: IProveedores): Observable<EntityResponseType> {
    return this.http.post<IProveedores>(this.resourceUrl, proveedores, { observe: 'response' });
  }

  update(proveedores: IProveedores): Observable<EntityResponseType> {
    return this.http.put<IProveedores>(this.resourceUrl, proveedores, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProveedores>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProveedores[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
