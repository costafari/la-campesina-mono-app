import { Moment } from 'moment';

export interface IClientes {
  id?: number;
  activo?: number;
  apellidos?: string;
  createdAt?: Moment;
  direcion?: string;
  email?: string;
  nombreContacto?: string;
  nombreEmpresa?: string;
  nombres?: string;
  notas?: string;
  sitioWeb?: string;
  telefonoFijo?: string;
  telefonoFijo2?: string;
  telefonoMovil?: string;
  telefonoMovil2?: string;
}

export class Clientes implements IClientes {
  constructor(
    public id?: number,
    public activo?: number,
    public apellidos?: string,
    public createdAt?: Moment,
    public direcion?: string,
    public email?: string,
    public nombreContacto?: string,
    public nombreEmpresa?: string,
    public nombres?: string,
    public notas?: string,
    public sitioWeb?: string,
    public telefonoFijo?: string,
    public telefonoFijo2?: string,
    public telefonoMovil?: string,
    public telefonoMovil2?: string
  ) {}
}
