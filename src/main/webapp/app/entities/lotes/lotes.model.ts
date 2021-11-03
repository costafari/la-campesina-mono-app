import * as dayjs from 'dayjs';
import { IDetalles } from 'app/entities/detalles/detalles.model';
import { IProveedores } from 'app/entities/proveedores/proveedores.model';
import { IProductos } from 'app/entities/productos/productos.model';
import { IPrecios } from 'app/entities/precios/precios.model';

export interface ILotes {
  id?: number;
  cantidad?: number | null;
  fechaEntrada?: dayjs.Dayjs | null;
  lote?: string | null;
  detalles?: IDetalles[] | null;
  proveedores?: IProveedores | null;
  productos?: IProductos | null;
  precios?: IPrecios | null;
}

export class Lotes implements ILotes {
  constructor(
    public id?: number,
    public cantidad?: number | null,
    public fechaEntrada?: dayjs.Dayjs | null,
    public lote?: string | null,
    public detalles?: IDetalles[] | null,
    public proveedores?: IProveedores | null,
    public productos?: IProductos | null,
    public precios?: IPrecios | null
  ) {}
}

export function getLotesIdentifier(lotes: ILotes): number | undefined {
  return lotes.id;
}
