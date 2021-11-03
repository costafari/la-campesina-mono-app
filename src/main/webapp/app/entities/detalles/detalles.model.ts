import { IFacturas } from 'app/entities/facturas/facturas.model';
import { ILotes } from 'app/entities/lotes/lotes.model';

export interface IDetalles {
  id?: number;
  cantidad?: number;
  total?: number | null;
  facturas?: IFacturas | null;
  lotes?: ILotes | null;
}

export class Detalles implements IDetalles {
  constructor(
    public id?: number,
    public cantidad?: number,
    public total?: number | null,
    public facturas?: IFacturas | null,
    public lotes?: ILotes | null
  ) {}
}

export function getDetallesIdentifier(detalles: IDetalles): number | undefined {
  return detalles.id;
}
