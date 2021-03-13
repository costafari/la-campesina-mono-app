import { Moment } from 'moment';
import { IClientes } from 'app/shared/model/clientes.model';
import { IAbonoFacturas } from 'app/shared/model/abono-facturas.model';
import { IFacturasDetalle } from 'app/shared/model/facturas-detalle.model';

export interface IFacturasMaster {
  id?: number;
  numeroFactura?: number;
  fechaFactura?: Moment;
  condicionPago?: boolean;
  clientes?: IClientes[];
  abonoFacturas?: IAbonoFacturas[];
  facturaMasterId?: IFacturasDetalle;
}

export class FacturasMaster implements IFacturasMaster {
  constructor(
    public id?: number,
    public numeroFactura?: number,
    public fechaFactura?: Moment,
    public condicionPago?: boolean,
    public clientes?: IClientes[],
    public abonoFacturas?: IAbonoFacturas[],
    public facturaMasterId?: IFacturasDetalle
  ) {
    this.condicionPago = this.condicionPago || false;
  }
}
