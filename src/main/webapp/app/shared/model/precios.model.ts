import { Moment } from 'moment';
import { ITickets } from 'app/shared/model/tickets.model';
import { ILotes } from 'app/shared/model/lotes.model';
import { IClientes } from 'app/shared/model/clientes.model';

export interface IPrecios {
  id?: number;
  fechaFin?: Moment;
  fechaInicio?: Moment;
  precio?: number;
  tickets?: ITickets[];
  loteId?: ILotes;
  clienteId?: IClientes;
}

export class Precios implements IPrecios {
  constructor(
    public id?: number,
    public fechaFin?: Moment,
    public fechaInicio?: Moment,
    public precio?: number,
    public tickets?: ITickets[],
    public loteId?: ILotes,
    public clienteId?: IClientes
  ) {}
}
