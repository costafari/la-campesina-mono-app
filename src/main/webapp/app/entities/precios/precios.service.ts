import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPrecios } from 'app/shared/model/precios.model';

type EntityResponseType = HttpResponse<IPrecios>;
type EntityArrayResponseType = HttpResponse<IPrecios[]>;

@Injectable({ providedIn: 'root' })
export class PreciosService {
  public resourceUrl = SERVER_API_URL + 'api/precios';

  constructor(protected http: HttpClient) {}

  create(precios: IPrecios): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(precios);
    return this.http
      .post<IPrecios>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(precios: IPrecios): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(precios);
    return this.http
      .put<IPrecios>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPrecios>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPrecios[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(precios: IPrecios): IPrecios {
    const copy: IPrecios = Object.assign({}, precios, {
      fechaFin: precios.fechaFin && precios.fechaFin.isValid() ? precios.fechaFin.format(DATE_FORMAT) : undefined,
      fechaInicio: precios.fechaInicio && precios.fechaInicio.isValid() ? precios.fechaInicio.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaFin = res.body.fechaFin ? moment(res.body.fechaFin) : undefined;
      res.body.fechaInicio = res.body.fechaInicio ? moment(res.body.fechaInicio) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((precios: IPrecios) => {
        precios.fechaFin = precios.fechaFin ? moment(precios.fechaFin) : undefined;
        precios.fechaInicio = precios.fechaInicio ? moment(precios.fechaInicio) : undefined;
      });
    }
    return res;
  }
}
