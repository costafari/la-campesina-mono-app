import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'tickets',
        loadChildren: () => import('./tickets/tickets.module').then(m => m.LacampesinaTicketsModule),
      },
      {
        path: 'precios',
        loadChildren: () => import('./precios/precios.module').then(m => m.LacampesinaPreciosModule),
      },
      {
        path: 'clientes',
        loadChildren: () => import('./clientes/clientes.module').then(m => m.LacampesinaClientesModule),
      },
      {
        path: 'lotes',
        loadChildren: () => import('./lotes/lotes.module').then(m => m.LacampesinaLotesModule),
      },
      {
        path: 'productos',
        loadChildren: () => import('./productos/productos.module').then(m => m.LacampesinaProductosModule),
      },
      {
        path: 'proveedores',
        loadChildren: () => import('./proveedores/proveedores.module').then(m => m.LacampesinaProveedoresModule),
      },
      {
        path: 'generalidades',
        loadChildren: () => import('./generalidades/generalidades.module').then(m => m.LacampesinaGeneralidadesModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class LacampesinaEntityModule {}
