jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILotes, Lotes } from '../lotes.model';
import { LotesService } from '../service/lotes.service';

import { LotesRoutingResolveService } from './lotes-routing-resolve.service';

describe('Service Tests', () => {
  describe('Lotes routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LotesRoutingResolveService;
    let service: LotesService;
    let resultLotes: ILotes | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LotesRoutingResolveService);
      service = TestBed.inject(LotesService);
      resultLotes = undefined;
    });

    describe('resolve', () => {
      it('should return ILotes returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLotes = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLotes).toEqual({ id: 123 });
      });

      it('should return new ILotes if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLotes = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLotes).toEqual(new Lotes());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Lotes })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLotes = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLotes).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
