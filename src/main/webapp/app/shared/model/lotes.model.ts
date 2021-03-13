import { Moment } from 'moment';
import { IPrecios } from 'app/shared/model/precios.model';
import { IProductos } from 'app/shared/model/productos.model';
import { IProveedores } from 'app/shared/model/proveedores.model';
import { IFacturasDetalle } from 'app/shared/model/facturas-detalle.model';

export interface ILotes {
  id?: number;
  cantidad?: number;
  fechaEntrada?: Moment;
  lote?: string;
  precios?: IPrecios[];
  productoId?: IProductos;
  proveedorId?: IProveedores;
  loteId?: IFacturasDetalle;
}

export class Lotes implements ILotes {
  constructor(
    public id?: number,
    public cantidad?: number,
    public fechaEntrada?: Moment,
    public lote?: string,
    public precios?: IPrecios[],
    public productoId?: IProductos,
    public proveedorId?: IProveedores,
    public loteId?: IFacturasDetalle
  ) {}
}
