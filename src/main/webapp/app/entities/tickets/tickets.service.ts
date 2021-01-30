import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITickets } from 'app/shared/model/tickets.model';

type EntityResponseType = HttpResponse<ITickets>;
type EntityArrayResponseType = HttpResponse<ITickets[]>;

@Injectable({ providedIn: 'root' })
export class TicketsService {
  public resourceUrl = SERVER_API_URL + 'api/tickets';

  constructor(protected http: HttpClient) {}

  create(tickets: ITickets): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tickets);
    return this.http
      .post<ITickets>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(tickets: ITickets): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tickets);
    return this.http
      .put<ITickets>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITickets>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITickets[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(tickets: ITickets): ITickets {
    const copy: ITickets = Object.assign({}, tickets, {
      createdAt: tickets.createdAt && tickets.createdAt.isValid() ? tickets.createdAt.toJSON() : undefined,
      fechaExpedicion: tickets.fechaExpedicion && tickets.fechaExpedicion.isValid() ? tickets.fechaExpedicion.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? moment(res.body.createdAt) : undefined;
      res.body.fechaExpedicion = res.body.fechaExpedicion ? moment(res.body.fechaExpedicion) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((tickets: ITickets) => {
        tickets.createdAt = tickets.createdAt ? moment(tickets.createdAt) : undefined;
        tickets.fechaExpedicion = tickets.fechaExpedicion ? moment(tickets.fechaExpedicion) : undefined;
      });
    }
    return res;
  }
}
