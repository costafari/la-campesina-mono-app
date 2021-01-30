import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IGeneralidades } from 'app/shared/model/generalidades.model';

type EntityResponseType = HttpResponse<IGeneralidades>;
type EntityArrayResponseType = HttpResponse<IGeneralidades[]>;

@Injectable({ providedIn: 'root' })
export class GeneralidadesService {
  public resourceUrl = SERVER_API_URL + 'api/generalidades';

  constructor(protected http: HttpClient) {}

  create(generalidades: IGeneralidades): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(generalidades);
    return this.http
      .post<IGeneralidades>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(generalidades: IGeneralidades): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(generalidades);
    return this.http
      .put<IGeneralidades>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IGeneralidades>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IGeneralidades[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(generalidades: IGeneralidades): IGeneralidades {
    const copy: IGeneralidades = Object.assign({}, generalidades, {
      createdAt: generalidades.createdAt && generalidades.createdAt.isValid() ? generalidades.createdAt.toJSON() : undefined,
      fechaInicio: generalidades.fechaInicio && generalidades.fechaInicio.isValid() ? generalidades.fechaInicio.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? moment(res.body.createdAt) : undefined;
      res.body.fechaInicio = res.body.fechaInicio ? moment(res.body.fechaInicio) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((generalidades: IGeneralidades) => {
        generalidades.createdAt = generalidades.createdAt ? moment(generalidades.createdAt) : undefined;
        generalidades.fechaInicio = generalidades.fechaInicio ? moment(generalidades.fechaInicio) : undefined;
      });
    }
    return res;
  }
}
