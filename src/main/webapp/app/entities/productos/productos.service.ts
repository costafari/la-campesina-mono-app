import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IProductos } from 'app/shared/model/productos.model';

type EntityResponseType = HttpResponse<IProductos>;
type EntityArrayResponseType = HttpResponse<IProductos[]>;

@Injectable({ providedIn: 'root' })
export class ProductosService {
  public resourceUrl = SERVER_API_URL + 'api/productos';

  constructor(protected http: HttpClient) {}

  create(productos: IProductos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productos);
    return this.http
      .post<IProductos>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(productos: IProductos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productos);
    return this.http
      .put<IProductos>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProductos>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProductos[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(productos: IProductos): IProductos {
    const copy: IProductos = Object.assign({}, productos, {
      createdAt: productos.createdAt && productos.createdAt.isValid() ? productos.createdAt.toJSON() : undefined,
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
      res.body.forEach((productos: IProductos) => {
        productos.createdAt = productos.createdAt ? moment(productos.createdAt) : undefined;
      });
    }
    return res;
  }
}
