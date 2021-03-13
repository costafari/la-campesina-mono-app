import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { FacturasMasterService } from 'app/entities/facturas-master/facturas-master.service';
import { IFacturasMaster, FacturasMaster } from 'app/shared/model/facturas-master.model';

describe('Service Tests', () => {
  describe('FacturasMaster Service', () => {
    let injector: TestBed;
    let service: FacturasMasterService;
    let httpMock: HttpTestingController;
    let elemDefault: IFacturasMaster;
    let expectedResult: IFacturasMaster | IFacturasMaster[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(FacturasMasterService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new FacturasMaster(0, 0, currentDate, false);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            fechaFactura: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a FacturasMaster', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            fechaFactura: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaFactura: currentDate,
          },
          returnedFromService
        );

        service.create(new FacturasMaster()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FacturasMaster', () => {
        const returnedFromService = Object.assign(
          {
            numeroFactura: 1,
            fechaFactura: currentDate.format(DATE_TIME_FORMAT),
            condicionPago: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaFactura: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of FacturasMaster', () => {
        const returnedFromService = Object.assign(
          {
            numeroFactura: 1,
            fechaFactura: currentDate.format(DATE_TIME_FORMAT),
            condicionPago: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaFactura: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a FacturasMaster', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
