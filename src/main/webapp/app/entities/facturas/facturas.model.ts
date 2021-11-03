import * as dayjs from 'dayjs';
import { IClientes } from 'app/entities/clientes/clientes.model';
import { IDetalles } from 'app/entities/detalles/detalles.model';
import { IAbonos } from 'app/entities/abonos/abonos.model';

export interface IFacturas {
  id?: number;
  numeroFactura?: number;
  fechaFactura?: dayjs.Dayjs | null;
  condicionPago?: boolean | null;
  clientes?: IClientes | null;
  detalles?: IDetalles[] | null;
  abonos?: IAbonos[] | null;
}

export class Facturas implements IFacturas {
  constructor(
    public id?: number,
    public numeroFactura?: number,
    public fechaFactura?: dayjs.Dayjs | null,
    public condicionPago?: boolean | null,
    public clientes?: IClientes | null,
    public detalles?: IDetalles[] | null,
    public abonos?: IAbonos[] | null
  ) {
    this.condicionPago = this.condicionPago ?? false;
  }
}

export function getFacturasIdentifier(facturas: IFacturas): number | undefined {
  return facturas.id;
}
