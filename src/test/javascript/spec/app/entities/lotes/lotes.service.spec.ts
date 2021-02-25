import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { LotesService } from 'app/entities/lotes/lotes.service';
import { ILotes, Lotes } from 'app/shared/model/lotes.model';

describe('Service Tests', () => {
  describe('Lotes Service', () => {
    let injector: TestBed;
    let service: LotesService;
    let httpMock: HttpTestingController;
    let elemDefault: ILotes;
    let expectedResult: ILotes | ILotes[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(LotesService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Lotes(0, 0, currentDate, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            fechaEntrada: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Lotes', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            fechaEntrada: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaEntrada: currentDate,
          },
          returnedFromService
        );

        service.create(new Lotes()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Lotes', () => {
        const returnedFromService = Object.assign(
          {
            cantidad: 1,
            fechaEntrada: currentDate.format(DATE_FORMAT),
            lote: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaEntrada: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Lotes', () => {
        const returnedFromService = Object.assign(
          {
            cantidad: 1,
            fechaEntrada: currentDate.format(DATE_FORMAT),
            lote: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaEntrada: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Lotes', () => {
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
