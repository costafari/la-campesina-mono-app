import { IFacturasMaster } from 'app/shared/model/facturas-master.model';
import { ILotes } from 'app/shared/model/lotes.model';
import { IPrecios } from 'app/shared/model/precios.model';

export interface IFacturasDetalle {
  id?: number;
  cantidad?: number;
  total?: number;
  facturasMasters?: IFacturasMaster[];
  lotes?: ILotes[];
  precios?: IPrecios[];
}

export class FacturasDetalle implements IFacturasDetalle {
  constructor(
    public id?: number,
    public cantidad?: number,
    public total?: number,
    public facturasMasters?: IFacturasMaster[],
    public lotes?: ILotes[],
    public precios?: IPrecios[]
  ) {}
}
