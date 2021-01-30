import { Moment } from 'moment';

export interface ILotes {
  id?: number;
  cantidad?: number;
  createdAt?: Moment;
  fechaEntrada?: Moment;
  lote?: string;
}

export class Lotes implements ILotes {
  constructor(
    public id?: number,
    public cantidad?: number,
    public createdAt?: Moment,
    public fechaEntrada?: Moment,
    public lote?: string
  ) {}
}
