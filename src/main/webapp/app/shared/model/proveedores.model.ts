import { Moment } from 'moment';

export interface IProveedores {
  id?: number;
  createdAt?: Moment;
  direccion?: string;
  nombreContacto?: string;
  nombreEmpresa?: string;
  notas?: string;
  sitioWeb?: string;
  telefonoFijo?: string;
  telefonoFijo2?: string;
  telefonoMovil?: string;
  telefonoMovil2?: string;
}

export class Proveedores implements IProveedores {
  constructor(
    public id?: number,
    public createdAt?: Moment,
    public direccion?: string,
    public nombreContacto?: string,
    public nombreEmpresa?: string,
    public notas?: string,
    public sitioWeb?: string,
    public telefonoFijo?: string,
    public telefonoFijo2?: string,
    public telefonoMovil?: string,
    public telefonoMovil2?: string
  ) {}
}
