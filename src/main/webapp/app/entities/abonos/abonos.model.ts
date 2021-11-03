import { IFacturas } from 'app/entities/facturas/facturas.model';

export interface IAbonos {
  id?: number;
  saldoAnterior?: number;
  abono?: number;
  nuevoSaldo?: number | null;
  facturas?: IFacturas | null;
}

export class Abonos implements IAbonos {
  constructor(
    public id?: number,
    public saldoAnterior?: number,
    public abono?: number,
    public nuevoSaldo?: number | null,
    public facturas?: IFacturas | null
  ) {}
}

export function getAbonosIdentifier(abonos: IAbonos): number | undefined {
  return abonos.id;
}
