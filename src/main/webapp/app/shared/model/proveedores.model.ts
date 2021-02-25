import { ILotes } from 'app/shared/model/lotes.model';

export interface IProveedores {
  id?: number;
  direccion?: string;
  nombreContacto?: string;
  nombreEmpresa?: string;
  notas?: string;
  sitioWeb?: string;
  telefonoFijo?: number;
  telefonoFijo2?: number;
  telefonoMovil?: number;
  telefonoMovil2?: number;
  lotes?: ILotes[];
}

export class Proveedores implements IProveedores {
  constructor(
    public id?: number,
    public direccion?: string,
    public nombreContacto?: string,
    public nombreEmpresa?: string,
    public notas?: string,
    public sitioWeb?: string,
    public telefonoFijo?: number,
    public telefonoFijo2?: number,
    public telefonoMovil?: number,
    public telefonoMovil2?: number,
    public lotes?: ILotes[]
  ) {}
}
