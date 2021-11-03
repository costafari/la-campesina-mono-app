import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProveedores, getProveedoresIdentifier } from '../proveedores.model';

export type EntityResponseType = HttpResponse<IProveedores>;
export type EntityArrayResponseType = HttpResponse<IProveedores[]>;

@Injectable({ providedIn: 'root' })
export class ProveedoresService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/proveedores');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(proveedores: IProveedores): Observable<EntityResponseType> {
    return this.http.post<IProveedores>(this.resourceUrl, proveedores, { observe: 'response' });
  }

  update(proveedores: IProveedores): Observable<EntityResponseType> {
    return this.http.put<IProveedores>(`${this.resourceUrl}/${getProveedoresIdentifier(proveedores) as number}`, proveedores, {
      observe: 'response',
    });
  }

  partialUpdate(proveedores: IProveedores): Observable<EntityResponseType> {
    return this.http.patch<IProveedores>(`${this.resourceUrl}/${getProveedoresIdentifier(proveedores) as number}`, proveedores, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProveedores>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProveedores[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProveedoresToCollectionIfMissing(
    proveedoresCollection: IProveedores[],
    ...proveedoresToCheck: (IProveedores | null | undefined)[]
  ): IProveedores[] {
    const proveedores: IProveedores[] = proveedoresToCheck.filter(isPresent);
    if (proveedores.length > 0) {
      const proveedoresCollectionIdentifiers = proveedoresCollection.map(proveedoresItem => getProveedoresIdentifier(proveedoresItem)!);
      const proveedoresToAdd = proveedores.filter(proveedoresItem => {
        const proveedoresIdentifier = getProveedoresIdentifier(proveedoresItem);
        if (proveedoresIdentifier == null || proveedoresCollectionIdentifiers.includes(proveedoresIdentifier)) {
          return false;
        }
        proveedoresCollectionIdentifiers.push(proveedoresIdentifier);
        return true;
      });
      return [...proveedoresToAdd, ...proveedoresCollection];
    }
    return proveedoresCollection;
  }
}
