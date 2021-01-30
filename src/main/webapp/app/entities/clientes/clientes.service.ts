import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IClientes } from 'app/shared/model/clientes.model';

type EntityResponseType = HttpResponse<IClientes>;
type EntityArrayResponseType = HttpResponse<IClientes[]>;

@Injectable({ providedIn: 'root' })
export class ClientesService {
  public resourceUrl = SERVER_API_URL + 'api/clientes';

  constructor(protected http: HttpClient) {}

  create(clientes: IClientes): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(clientes);
    return this.http
      .post<IClientes>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(clientes: IClientes): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(clientes);
    return this.http
      .put<IClientes>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IClientes>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IClientes[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(clientes: IClientes): IClientes {
    const copy: IClientes = Object.assign({}, clientes, {
      createdAt: clientes.createdAt && clientes.createdAt.isValid() ? clientes.createdAt.toJSON() : undefined,
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
      res.body.forEach((clientes: IClientes) => {
        clientes.createdAt = clientes.createdAt ? moment(clientes.createdAt) : undefined;
      });
    }
    return res;
  }
}
