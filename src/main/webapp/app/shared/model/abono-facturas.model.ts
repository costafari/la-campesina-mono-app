import { IFacturasMaster } from 'app/shared/model/facturas-master.model';

export interface IAbonoFacturas {
  id?: number;
  saldoAnterior?: number;
  abono?: number;
  nuevoSaldo?: number;
  abonoId?: IFacturasMaster;
}

export class AbonoFacturas implements IAbonoFacturas {
  constructor(
    public id?: number,
    public saldoAnterior?: number,
    public abono?: number,
    public nuevoSaldo?: number,
    public abonoId?: IFacturasMaster
  ) {}
}
