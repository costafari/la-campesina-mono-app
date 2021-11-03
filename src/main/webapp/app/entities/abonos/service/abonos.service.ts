import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAbonos, getAbonosIdentifier } from '../abonos.model';

export type EntityResponseType = HttpResponse<IAbonos>;
export type EntityArrayResponseType = HttpResponse<IAbonos[]>;

@Injectable({ providedIn: 'root' })
export class AbonosService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/abonos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(abonos: IAbonos): Observable<EntityResponseType> {
    return this.http.post<IAbonos>(this.resourceUrl, abonos, { observe: 'response' });
  }

  update(abonos: IAbonos): Observable<EntityResponseType> {
    return this.http.put<IAbonos>(`${this.resourceUrl}/${getAbonosIdentifier(abonos) as number}`, abonos, { observe: 'response' });
  }

  partialUpdate(abonos: IAbonos): Observable<EntityResponseType> {
    return this.http.patch<IAbonos>(`${this.resourceUrl}/${getAbonosIdentifier(abonos) as number}`, abonos, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAbonos>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAbonos[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAbonosToCollectionIfMissing(abonosCollection: IAbonos[], ...abonosToCheck: (IAbonos | null | undefined)[]): IAbonos[] {
    const abonos: IAbonos[] = abonosToCheck.filter(isPresent);
    if (abonos.length > 0) {
      const abonosCollectionIdentifiers = abonosCollection.map(abonosItem => getAbonosIdentifier(abonosItem)!);
      const abonosToAdd = abonos.filter(abonosItem => {
        const abonosIdentifier = getAbonosIdentifier(abonosItem);
        if (abonosIdentifier == null || abonosCollectionIdentifiers.includes(abonosIdentifier)) {
          return false;
        }
        abonosCollectionIdentifiers.push(abonosIdentifier);
        return true;
      });
      return [...abonosToAdd, ...abonosCollection];
    }
    return abonosCollection;
  }
}
