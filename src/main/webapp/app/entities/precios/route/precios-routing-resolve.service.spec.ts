jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPrecios, Precios } from '../precios.model';
import { PreciosService } from '../service/precios.service';

import { PreciosRoutingResolveService } from './precios-routing-resolve.service';

describe('Service Tests', () => {
  describe('Precios routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PreciosRoutingResolveService;
    let service: PreciosService;
    let resultPrecios: IPrecios | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PreciosRoutingResolveService);
      service = TestBed.inject(PreciosService);
      resultPrecios = undefined;
    });

    describe('resolve', () => {
      it('should return IPrecios returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPrecios = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPrecios).toEqual({ id: 123 });
      });

      it('should return new IPrecios if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPrecios = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPrecios).toEqual(new Precios());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Precios })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPrecios = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPrecios).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
