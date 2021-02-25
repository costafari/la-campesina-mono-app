import { ILotes } from 'app/shared/model/lotes.model';

export interface IProductos {
  id?: number;
  descipcion?: string;
  nombre?: string;
  notas?: string;
  lotes?: ILotes[];
}

export class Productos implements IProductos {
  constructor(public id?: number, public descipcion?: string, public nombre?: string, public notas?: string, public lotes?: ILotes[]) {}
}
