import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILotes, getLotesIdentifier } from '../lotes.model';

export type EntityResponseType = HttpResponse<ILotes>;
export type EntityArrayResponseType = HttpResponse<ILotes[]>;

@Injectable({ providedIn: 'root' })
export class LotesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lotes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  // parametro url: debe ser /lotes
  getAllObjects(url: String): Observable<any[]> {
    return this.http.get<any[]>(this.resourceUrl + url);
  }

  create(lotes: ILotes): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lotes);
    return this.http
      .post<ILotes>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(lotes: ILotes): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lotes);
    return this.http
      .put<ILotes>(`${this.resourceUrl}/${getLotesIdentifier(lotes) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(lotes: ILotes): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lotes);
    return this.http
      .patch<ILotes>(`${this.resourceUrl}/${getLotesIdentifier(lotes) as number}`, copy, { observe: 'response' })
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

  addLotesToCollectionIfMissing(lotesCollection: ILotes[], ...lotesToCheck: (ILotes | null | undefined)[]): ILotes[] {
    const lotes: ILotes[] = lotesToCheck.filter(isPresent);
    if (lotes.length > 0) {
      const lotesCollectionIdentifiers = lotesCollection.map(lotesItem => getLotesIdentifier(lotesItem)!);
      const lotesToAdd = lotes.filter(lotesItem => {
        const lotesIdentifier = getLotesIdentifier(lotesItem);
        if (lotesIdentifier == null || lotesCollectionIdentifiers.includes(lotesIdentifier)) {
          return false;
        }
        lotesCollectionIdentifiers.push(lotesIdentifier);
        return true;
      });
      return [...lotesToAdd, ...lotesCollection];
    }
    return lotesCollection;
  }

  protected convertDateFromClient(lotes: ILotes): ILotes {
    return Object.assign({}, lotes, {
      fechaEntrada: lotes.fechaEntrada?.isValid() ? lotes.fechaEntrada.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaEntrada = res.body.fechaEntrada ? dayjs(res.body.fechaEntrada) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((lotes: ILotes) => {
        lotes.fechaEntrada = lotes.fechaEntrada ? dayjs(lotes.fechaEntrada) : undefined;
      });
    }
    return res;
  }
}
