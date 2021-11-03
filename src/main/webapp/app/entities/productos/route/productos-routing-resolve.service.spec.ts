jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IProductos, Productos } from '../productos.model';
import { ProductosService } from '../service/productos.service';

import { ProductosRoutingResolveService } from './productos-routing-resolve.service';

describe('Service Tests', () => {
  describe('Productos routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ProductosRoutingResolveService;
    let service: ProductosService;
    let resultProductos: IProductos | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ProductosRoutingResolveService);
      service = TestBed.inject(ProductosService);
      resultProductos = undefined;
    });

    describe('resolve', () => {
      it('should return IProductos returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultProductos = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultProductos).toEqual({ id: 123 });
      });

      it('should return new IProductos if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultProductos = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultProductos).toEqual(new Productos());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Productos })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultProductos = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultProductos).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
