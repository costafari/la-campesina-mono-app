import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDetalles, getDetallesIdentifier } from '../detalles.model';

export type EntityResponseType = HttpResponse<IDetalles>;
export type EntityArrayResponseType = HttpResponse<IDetalles[]>;

@Injectable({ providedIn: 'root' })
export class DetallesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/detalles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(detalles: IDetalles): Observable<EntityResponseType> {
    return this.http.post<IDetalles>(this.resourceUrl, detalles, { observe: 'response' });
  }

  update(detalles: IDetalles): Observable<EntityResponseType> {
    return this.http.put<IDetalles>(`${this.resourceUrl}/${getDetallesIdentifier(detalles) as number}`, detalles, { observe: 'response' });
  }

  partialUpdate(detalles: IDetalles): Observable<EntityResponseType> {
    return this.http.patch<IDetalles>(`${this.resourceUrl}/${getDetallesIdentifier(detalles) as number}`, detalles, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDetalles>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDetalles[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDetallesToCollectionIfMissing(detallesCollection: IDetalles[], ...detallesToCheck: (IDetalles | null | undefined)[]): IDetalles[] {
    const detalles: IDetalles[] = detallesToCheck.filter(isPresent);
    if (detalles.length > 0) {
      const detallesCollectionIdentifiers = detallesCollection.map(detallesItem => getDetallesIdentifier(detallesItem)!);
      const detallesToAdd = detalles.filter(detallesItem => {
        const detallesIdentifier = getDetallesIdentifier(detallesItem);
        if (detallesIdentifier == null || detallesCollectionIdentifiers.includes(detallesIdentifier)) {
          return false;
        }
        detallesCollectionIdentifiers.push(detallesIdentifier);
        return true;
      });
      return [...detallesToAdd, ...detallesCollection];
    }
    return detallesCollection;
  }
}
