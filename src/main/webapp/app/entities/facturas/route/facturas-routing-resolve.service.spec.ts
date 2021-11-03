jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFacturas, Facturas } from '../facturas.model';
import { FacturasService } from '../service/facturas.service';

import { FacturasRoutingResolveService } from './facturas-routing-resolve.service';

describe('Service Tests', () => {
  describe('Facturas routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FacturasRoutingResolveService;
    let service: FacturasService;
    let resultFacturas: IFacturas | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FacturasRoutingResolveService);
      service = TestBed.inject(FacturasService);
      resultFacturas = undefined;
    });

    describe('resolve', () => {
      it('should return IFacturas returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFacturas = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFacturas).toEqual({ id: 123 });
      });

      it('should return new IFacturas if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFacturas = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFacturas).toEqual(new Facturas());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Facturas })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFacturas = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFacturas).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
