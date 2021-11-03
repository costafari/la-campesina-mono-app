import { ILotes } from 'app/entities/lotes/lotes.model';

export interface IProveedores {
  id?: number;
  direccion?: string | null;
  nombreContacto?: string | null;
  nombreEmpresa?: string | null;
  notas?: string | null;
  sitioWeb?: string | null;
  telefonoFijo?: number | null;
  telefonoFijo2?: number | null;
  telefonoMovil?: number | null;
  telefonoMovil2?: number | null;
  lotes?: ILotes | null;
}

export class Proveedores implements IProveedores {
  constructor(
    public id?: number,
    public direccion?: string | null,
    public nombreContacto?: string | null,
    public nombreEmpresa?: string | null,
    public notas?: string | null,
    public sitioWeb?: string | null,
    public telefonoFijo?: number | null,
    public telefonoFijo2?: number | null,
    public telefonoMovil?: number | null,
    public telefonoMovil2?: number | null,
    public lotes?: ILotes | null
  ) {}
}

export function getProveedoresIdentifier(proveedores: IProveedores): number | undefined {
  return proveedores.id;
}
