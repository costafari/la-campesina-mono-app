import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IFacturasMaster } from 'app/shared/model/facturas-master.model';

type EntityResponseType = HttpResponse<IFacturasMaster>;
type EntityArrayResponseType = HttpResponse<IFacturasMaster[]>;

@Injectable({ providedIn: 'root' })
export class FacturasMasterService {
  public resourceUrl = SERVER_API_URL + 'api/facturas-masters';

  constructor(protected http: HttpClient) {}

  create(facturasMaster: IFacturasMaster): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(facturasMaster);
    return this.http
      .post<IFacturasMaster>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(facturasMaster: IFacturasMaster): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(facturasMaster);
    return this.http
      .put<IFacturasMaster>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFacturasMaster>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFacturasMaster[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(facturasMaster: IFacturasMaster): IFacturasMaster {
    const copy: IFacturasMaster = Object.assign({}, facturasMaster, {
      fechaFactura: facturasMaster.fechaFactura && facturasMaster.fechaFactura.isValid() ? facturasMaster.fechaFactura.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaFactura = res.body.fechaFactura ? moment(res.body.fechaFactura) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((facturasMaster: IFacturasMaster) => {
        facturasMaster.fechaFactura = facturasMaster.fechaFactura ? moment(facturasMaster.fechaFactura) : undefined;
      });
    }
    return res;
  }
}
