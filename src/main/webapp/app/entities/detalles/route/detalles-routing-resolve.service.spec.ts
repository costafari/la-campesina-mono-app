jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDetalles, Detalles } from '../detalles.model';
import { DetallesService } from '../service/detalles.service';

import { DetallesRoutingResolveService } from './detalles-routing-resolve.service';

describe('Service Tests', () => {
  describe('Detalles routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DetallesRoutingResolveService;
    let service: DetallesService;
    let resultDetalles: IDetalles | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DetallesRoutingResolveService);
      service = TestBed.inject(DetallesService);
      resultDetalles = undefined;
    });

    describe('resolve', () => {
      it('should return IDetalles returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDetalles = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDetalles).toEqual({ id: 123 });
      });

      it('should return new IDetalles if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDetalles = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDetalles).toEqual(new Detalles());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Detalles })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDetalles = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDetalles).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
