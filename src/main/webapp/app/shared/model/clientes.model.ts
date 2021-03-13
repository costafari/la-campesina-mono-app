import { IPrecios } from 'app/shared/model/precios.model';
import { IFacturasMaster } from 'app/shared/model/facturas-master.model';

export interface IClientes {
  id?: number;
  activo?: boolean;
  apellidos?: string;
  direcion?: string;
  email?: string;
  nombreContacto?: string;
  nombreEmpresa?: string;
  nombres?: string;
  notas?: string;
  sitioWeb?: string;
  telefonoFijo?: number;
  telefonoFijo2?: number;
  telefonoMovil?: number;
  telefonoMovil2?: number;
  precios?: IPrecios[];
  clienteId?: IFacturasMaster;
}

export class Clientes implements IClientes {
  constructor(
    public id?: number,
    public activo?: boolean,
    public apellidos?: string,
    public direcion?: string,
    public email?: string,
    public nombreContacto?: string,
    public nombreEmpresa?: string,
    public nombres?: string,
    public notas?: string,
    public sitioWeb?: string,
    public telefonoFijo?: number,
    public telefonoFijo2?: number,
    public telefonoMovil?: number,
    public telefonoMovil2?: number,
    public precios?: IPrecios[],
    public clienteId?: IFacturasMaster
  ) {
    this.activo = this.activo || false;
  }
}
