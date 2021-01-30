import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

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
    const copy = this.convertDateFromClient(proveedores);
    return this.http
      .post<IProveedores>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(proveedores: IProveedores): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(proveedores);
    return this.http
      .put<IProveedores>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProveedores>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProveedores[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(proveedores: IProveedores): IProveedores {
    const copy: IProveedores = Object.assign({}, proveedores, {
      createdAt: proveedores.createdAt && proveedores.createdAt.isValid() ? proveedores.createdAt.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? moment(res.body.createdAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((proveedores: IProveedores) => {
        proveedores.createdAt = proveedores.createdAt ? moment(proveedores.createdAt) : undefined;
      });
    }
    return res;
  }
}
