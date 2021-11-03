import * as dayjs from 'dayjs';
import { ILotes } from 'app/entities/lotes/lotes.model';

export interface IPrecios {
  id?: number;
  fechaFin?: dayjs.Dayjs | null;
  fechaInicio?: dayjs.Dayjs | null;
  precio?: number | null;
  lotes?: ILotes[] | null;
}

export class Precios implements IPrecios {
  constructor(
    public id?: number,
    public fechaFin?: dayjs.Dayjs | null,
    public fechaInicio?: dayjs.Dayjs | null,
    public precio?: number | null,
    public lotes?: ILotes[] | null
  ) {}
}

export function getPreciosIdentifier(precios: IPrecios): number | undefined {
  return precios.id;
}
