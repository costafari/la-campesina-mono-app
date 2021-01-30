import { Moment } from 'moment';

export interface ITickets {
  id?: number;
  cantidad?: number;
  createdAt?: Moment;
  fechaExpedicion?: Moment;
  total?: number;
}

export class Tickets implements ITickets {
  constructor(
    public id?: number,
    public cantidad?: number,
    public createdAt?: Moment,
    public fechaExpedicion?: Moment,
    public total?: number
  ) {}
}
