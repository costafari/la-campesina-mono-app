import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'precios',
        data: { pageTitle: 'Precios' },
        loadChildren: () => import('./precios/precios.module').then(m => m.PreciosModule),
      },
      {
        path: 'clientes',
        data: { pageTitle: 'Clientes' },
        loadChildren: () => import('./clientes/clientes.module').then(m => m.ClientesModule),
      },
      {
        path: 'lotes',
        data: { pageTitle: 'Lotes' },
        loadChildren: () => import('./lotes/lotes.module').then(m => m.LotesModule),
      },
      {
        path: 'productos',
        data: { pageTitle: 'Productos' },
        loadChildren: () => import('./productos/productos.module').then(m => m.ProductosModule),
      },
      {
        path: 'proveedores',
        data: { pageTitle: 'Proveedores' },
        loadChildren: () => import('./proveedores/proveedores.module').then(m => m.ProveedoresModule),
      },
      {
        path: 'facturas',
        data: { pageTitle: 'Facturas' },
        loadChildren: () => import('./facturas/facturas.module').then(m => m.FacturasModule),
      },
      {
        path: 'detalles',
        data: { pageTitle: 'Detalles' },
        loadChildren: () => import('./detalles/detalles.module').then(m => m.DetallesModule),
      },
      {
        path: 'abonos',
        data: { pageTitle: 'Abonos' },
        loadChildren: () => import('./abonos/abonos.module').then(m => m.AbonosModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
