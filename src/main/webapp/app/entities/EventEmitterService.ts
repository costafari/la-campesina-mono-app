import { EventEmitter, Injectable } from '@angular/core';
import { Subscription } from 'rxjs/internal/Subscription';

@Injectable({
  providedIn: 'root',
})
export class EventEmitterService {
  invokeEventoComponenteDetalle = new EventEmitter();
  subsVar: Subscription = new Subscription();

  constructor() {
    // do nothing.
  }

  onGuardarDetalleDesdeEncabezado(): void {
    this.invokeEventoComponenteDetalle.emit();
  }
}
