import { Moment } from 'moment';

export interface IPrecios {
  id?: number;
  createdAt?: Moment;
  fechaFin?: Moment;
  fechaInicio?: Moment;
  precio?: number;
}

export class Precios implements IPrecios {
  constructor(
    public id?: number,
    public createdAt?: Moment,
    public fechaFin?: Moment,
    public fechaInicio?: Moment,
    public precio?: number
  ) {}
}
