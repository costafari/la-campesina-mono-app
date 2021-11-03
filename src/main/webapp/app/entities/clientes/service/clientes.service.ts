import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClientes, getClientesIdentifier } from '../clientes.model';

export type EntityResponseType = HttpResponse<IClientes>;
export type EntityArrayResponseType = HttpResponse<IClientes[]>;

@Injectable({ providedIn: 'root' })
export class ClientesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/clientes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(clientes: IClientes): Observable<EntityResponseType> {
    return this.http.post<IClientes>(this.resourceUrl, clientes, { observe: 'response' });
  }

  update(clientes: IClientes): Observable<EntityResponseType> {
    return this.http.put<IClientes>(`${this.resourceUrl}/${getClientesIdentifier(clientes) as number}`, clientes, { observe: 'response' });
  }

  partialUpdate(clientes: IClientes): Observable<EntityResponseType> {
    return this.http.patch<IClientes>(`${this.resourceUrl}/${getClientesIdentifier(clientes) as number}`, clientes, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IClientes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClientes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addClientesToCollectionIfMissing(clientesCollection: IClientes[], ...clientesToCheck: (IClientes | null | undefined)[]): IClientes[] {
    const clientes: IClientes[] = clientesToCheck.filter(isPresent);
    if (clientes.length > 0) {
      const clientesCollectionIdentifiers = clientesCollection.map(clientesItem => getClientesIdentifier(clientesItem)!);
      const clientesToAdd = clientes.filter(clientesItem => {
        const clientesIdentifier = getClientesIdentifier(clientesItem);
        if (clientesIdentifier == null || clientesCollectionIdentifiers.includes(clientesIdentifier)) {
          return false;
        }
        clientesCollectionIdentifiers.push(clientesIdentifier);
        return true;
      });
      return [...clientesToAdd, ...clientesCollection];
    }
    return clientesCollection;
  }
}
