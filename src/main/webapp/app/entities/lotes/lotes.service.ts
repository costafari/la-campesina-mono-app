import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ILotes } from 'app/shared/model/lotes.model';

type EntityResponseType = HttpResponse<ILotes>;
type EntityArrayResponseType = HttpResponse<ILotes[]>;

@Injectable({ providedIn: 'root' })
export class LotesService {
  public resourceUrl = SERVER_API_URL + 'api/lotes';

  constructor(protected http: HttpClient) {}

  create(lotes: ILotes): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lotes);
    return this.http
      .post<ILotes>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(lotes: ILotes): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lotes);
    return this.http
      .put<ILotes>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILotes>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILotes[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(lotes: ILotes): ILotes {
    const copy: ILotes = Object.assign({}, lotes, {
      createdAt: lotes.createdAt && lotes.createdAt.isValid() ? lotes.createdAt.toJSON() : undefined,
      fechaEntrada: lotes.fechaEntrada && lotes.fechaEntrada.isValid() ? lotes.fechaEntrada.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? moment(res.body.createdAt) : undefined;
      res.body.fechaEntrada = res.body.fechaEntrada ? moment(res.body.fechaEntrada) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((lotes: ILotes) => {
        lotes.createdAt = lotes.createdAt ? moment(lotes.createdAt) : undefined;
        lotes.fechaEntrada = lotes.fechaEntrada ? moment(lotes.fechaEntrada) : undefined;
      });
    }
    return res;
  }
}
