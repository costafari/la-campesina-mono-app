import { Moment } from 'moment';
import { ILotes } from 'app/shared/model/lotes.model';
import { IClientes } from 'app/shared/model/clientes.model';
import { IFacturasDetalle } from 'app/shared/model/facturas-detalle.model';

export interface IPrecios {
  id?: number;
  fechaFin?: Moment;
  fechaInicio?: Moment;
  precio?: number;
  loteId?: ILotes;
  clienteId?: IClientes;
  precioId?: IFacturasDetalle;
}

export class Precios implements IPrecios {
  constructor(
    public id?: number,
    public fechaFin?: Moment,
    public fechaInicio?: Moment,
    public precio?: number,
    public loteId?: ILotes,
    public clienteId?: IClientes,
    public precioId?: IFacturasDetalle
  ) {}
}
