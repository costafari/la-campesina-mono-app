import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ILotes, Lotes } from '../lotes.model';

import { LotesService } from './lotes.service';

describe('Service Tests', () => {
  describe('Lotes Service', () => {
    let service: LotesService;
    let httpMock: HttpTestingController;
    let elemDefault: ILotes;
    let expectedResult: ILotes | ILotes[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LotesService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        cantidad: 0,
        fechaEntrada: currentDate,
        lote: 'AAAAAAA',
      };
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
            id: 1,
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

      it('should partial update a Lotes', () => {
        const patchObject = Object.assign(
          {
            cantidad: 1,
            fechaEntrada: currentDate.format(DATE_FORMAT),
            lote: 'BBBBBB',
          },
          new Lotes()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            fechaEntrada: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Lotes', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
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

      describe('addLotesToCollectionIfMissing', () => {
        it('should add a Lotes to an empty array', () => {
          const lotes: ILotes = { id: 123 };
          expectedResult = service.addLotesToCollectionIfMissing([], lotes);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lotes);
        });

        it('should not add a Lotes to an array that contains it', () => {
          const lotes: ILotes = { id: 123 };
          const lotesCollection: ILotes[] = [
            {
              ...lotes,
            },
            { id: 456 },
          ];
          expectedResult = service.addLotesToCollectionIfMissing(lotesCollection, lotes);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Lotes to an array that doesn't contain it", () => {
          const lotes: ILotes = { id: 123 };
          const lotesCollection: ILotes[] = [{ id: 456 }];
          expectedResult = service.addLotesToCollectionIfMissing(lotesCollection, lotes);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lotes);
        });

        it('should add only unique Lotes to an array', () => {
          const lotesArray: ILotes[] = [{ id: 123 }, { id: 456 }, { id: 20842 }];
          const lotesCollection: ILotes[] = [{ id: 123 }];
          expectedResult = service.addLotesToCollectionIfMissing(lotesCollection, ...lotesArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const lotes: ILotes = { id: 123 };
          const lotes2: ILotes = { id: 456 };
          expectedResult = service.addLotesToCollectionIfMissing([], lotes, lotes2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lotes);
          expect(expectedResult).toContain(lotes2);
        });

        it('should accept null and undefined values', () => {
          const lotes: ILotes = { id: 123 };
          expectedResult = service.addLotesToCollectionIfMissing([], null, lotes, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lotes);
        });

        it('should return initial array if no Lotes is added', () => {
          const lotesCollection: ILotes[] = [{ id: 123 }];
          expectedResult = service.addLotesToCollectionIfMissing(lotesCollection, undefined, null);
          expect(expectedResult).toEqual(lotesCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
