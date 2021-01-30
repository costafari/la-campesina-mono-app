import { Moment } from 'moment';

export interface IProductos {
  id?: number;
  createdAt?: Moment;
  descipcion?: string;
  nombre?: string;
  notas?: string;
}

export class Productos implements IProductos {
  constructor(public id?: number, public createdAt?: Moment, public descipcion?: string, public nombre?: string, public notas?: string) {}
}
