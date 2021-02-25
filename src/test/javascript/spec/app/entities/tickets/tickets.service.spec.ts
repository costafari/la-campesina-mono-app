import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { TicketsService } from 'app/entities/tickets/tickets.service';
import { ITickets, Tickets } from 'app/shared/model/tickets.model';

describe('Service Tests', () => {
  describe('Tickets Service', () => {
    let injector: TestBed;
    let service: TicketsService;
    let httpMock: HttpTestingController;
    let elemDefault: ITickets;
    let expectedResult: ITickets | ITickets[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(TicketsService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Tickets(0, 0, currentDate, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            fechaExpedicion: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Tickets', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            fechaExpedicion: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaExpedicion: currentDate,
          },
          returnedFromService
        );

        service.create(new Tickets()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Tickets', () => {
        const returnedFromService = Object.assign(
          {
            cantidad: 1,
            fechaExpedicion: currentDate.format(DATE_FORMAT),
            total: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaExpedicion: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Tickets', () => {
        const returnedFromService = Object.assign(
          {
            cantidad: 1,
            fechaExpedicion: currentDate.format(DATE_FORMAT),
            total: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaExpedicion: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Tickets', () => {
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
