import { Moment } from 'moment';
import { IPrecios } from 'app/shared/model/precios.model';

export interface ITickets {
  id?: number;
  cantidad?: number;
  fechaExpedicion?: Moment;
  total?: number;
  precioId?: IPrecios;
}

export class Tickets implements ITickets {
  constructor(
    public id?: number,
    public cantidad?: number,
    public fechaExpedicion?: Moment,
    public total?: number,
    public precioId?: IPrecios
  ) {}
}
