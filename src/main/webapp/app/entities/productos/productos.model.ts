import { ILotes } from 'app/entities/lotes/lotes.model';

export interface IProductos {
  id?: number;
  descipcion?: string | null;
  nombre?: string | null;
  notas?: string | null;
  lotes?: ILotes[] | null;
}

export class Productos implements IProductos {
  constructor(
    public id?: number,
    public descipcion?: string | null,
    public nombre?: string | null,
    public notas?: string | null,
    public lotes?: ILotes[] | null
  ) {}
}

export function getProductosIdentifier(productos: IProductos): number | undefined {
  return productos.id;
}
