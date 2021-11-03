import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPrecios, getPreciosIdentifier } from '../precios.model';

export type EntityResponseType = HttpResponse<IPrecios>;
export type EntityArrayResponseType = HttpResponse<IPrecios[]>;

@Injectable({ providedIn: 'root' })
export class PreciosService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/precios');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(precios: IPrecios): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(precios);
    return this.http
      .post<IPrecios>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(precios: IPrecios): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(precios);
    return this.http
      .put<IPrecios>(`${this.resourceUrl}/${getPreciosIdentifier(precios) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(precios: IPrecios): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(precios);
    return this.http
      .patch<IPrecios>(`${this.resourceUrl}/${getPreciosIdentifier(precios) as number}`, copy, { observe: 'response' })
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

  addPreciosToCollectionIfMissing(preciosCollection: IPrecios[], ...preciosToCheck: (IPrecios | null | undefined)[]): IPrecios[] {
    const precios: IPrecios[] = preciosToCheck.filter(isPresent);
    if (precios.length > 0) {
      const preciosCollectionIdentifiers = preciosCollection.map(preciosItem => getPreciosIdentifier(preciosItem)!);
      const preciosToAdd = precios.filter(preciosItem => {
        const preciosIdentifier = getPreciosIdentifier(preciosItem);
        if (preciosIdentifier == null || preciosCollectionIdentifiers.includes(preciosIdentifier)) {
          return false;
        }
        preciosCollectionIdentifiers.push(preciosIdentifier);
        return true;
      });
      return [...preciosToAdd, ...preciosCollection];
    }
    return preciosCollection;
  }

  protected convertDateFromClient(precios: IPrecios): IPrecios {
    return Object.assign({}, precios, {
      fechaFin: precios.fechaFin?.isValid() ? precios.fechaFin.format(DATE_FORMAT) : undefined,
      fechaInicio: precios.fechaInicio?.isValid() ? precios.fechaInicio.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaFin = res.body.fechaFin ? dayjs(res.body.fechaFin) : undefined;
      res.body.fechaInicio = res.body.fechaInicio ? dayjs(res.body.fechaInicio) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((precios: IPrecios) => {
        precios.fechaFin = precios.fechaFin ? dayjs(precios.fechaFin) : undefined;
        precios.fechaInicio = precios.fechaInicio ? dayjs(precios.fechaInicio) : undefined;
      });
    }
    return res;
  }
}
