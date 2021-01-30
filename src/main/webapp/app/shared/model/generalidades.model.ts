import { Moment } from 'moment';

export interface IGeneralidades {
  id?: number;
  createdAt?: Moment;
  fechaInicio?: Moment;
  nombreEmpresa?: string;
  nombrePropietario?: string;
}

export class Generalidades implements IGeneralidades {
  constructor(
    public id?: number,
    public createdAt?: Moment,
    public fechaInicio?: Moment,
    public nombreEmpresa?: string,
    public nombrePropietario?: string
  ) {}
}
