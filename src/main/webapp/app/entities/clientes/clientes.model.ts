import { IFacturas } from 'app/entities/facturas/facturas.model';

export interface IClientes {
  id?: number;
  activo?: boolean | null;
  apellidos?: string | null;
  direcion?: string | null;
  email?: string | null;
  nombreContacto?: string | null;
  nombreEmpresa?: string | null;
  nombres?: string | null;
  notas?: string | null;
  sitioWeb?: string | null;
  telefonoFijo?: number | null;
  telefonoFijo2?: number | null;
  telefonoMovil?: number | null;
  telefonoMovil2?: number | null;
  facturas?: IFacturas[] | null;
}

export class Clientes implements IClientes {
  constructor(
    public id?: number,
    public activo?: boolean | null,
    public apellidos?: string | null,
    public direcion?: string | null,
    public email?: string | null,
    public nombreContacto?: string | null,
    public nombreEmpresa?: string | null,
    public nombres?: string | null,
    public notas?: string | null,
    public sitioWeb?: string | null,
    public telefonoFijo?: number | null,
    public telefonoFijo2?: number | null,
    public telefonoMovil?: number | null,
    public telefonoMovil2?: number | null,
    public facturas?: IFacturas[] | null
  ) {
    this.activo = this.activo ?? false;
  }
}

export function getClientesIdentifier(clientes: IClientes): number | undefined {
  return clientes.id;
}
