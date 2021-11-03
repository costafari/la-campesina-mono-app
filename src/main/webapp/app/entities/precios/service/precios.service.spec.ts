import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPrecios, Precios } from '../precios.model';

import { PreciosService } from './precios.service';

describe('Service Tests', () => {
  describe('Precios Service', () => {
    let service: PreciosService;
    let httpMock: HttpTestingController;
    let elemDefault: IPrecios;
    let expectedResult: IPrecios | IPrecios[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PreciosService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        fechaFin: currentDate,
        fechaInicio: currentDate,
        precio: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            fechaFin: currentDate.format(DATE_FORMAT),
            fechaInicio: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Precios', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            fechaFin: currentDate.format(DATE_FORMAT),
            fechaInicio: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaFin: currentDate,
            fechaInicio: currentDate,
          },
          returnedFromService
        );

        service.create(new Precios()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Precios', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            fechaFin: currentDate.format(DATE_FORMAT),
            fechaInicio: currentDate.format(DATE_FORMAT),
            precio: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaFin: currentDate,
            fechaInicio: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Precios', () => {
        const patchObject = Object.assign(
          {
            fechaFin: currentDate.format(DATE_FORMAT),
            fechaInicio: currentDate.format(DATE_FORMAT),
            precio: 1,
          },
          new Precios()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            fechaFin: currentDate,
            fechaInicio: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Precios', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            fechaFin: currentDate.format(DATE_FORMAT),
            fechaInicio: currentDate.format(DATE_FORMAT),
            precio: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaFin: currentDate,
            fechaInicio: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Precios', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPreciosToCollectionIfMissing', () => {
        it('should add a Precios to an empty array', () => {
          const precios: IPrecios = { id: 123 };
          expectedResult = service.addPreciosToCollectionIfMissing([], precios);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(precios);
        });

        it('should not add a Precios to an array that contains it', () => {
          const precios: IPrecios = { id: 123 };
          const preciosCollection: IPrecios[] = [
            {
              ...precios,
            },
            { id: 456 },
          ];
          expectedResult = service.addPreciosToCollectionIfMissing(preciosCollection, precios);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Precios to an array that doesn't contain it", () => {
          const precios: IPrecios = { id: 123 };
          const preciosCollection: IPrecios[] = [{ id: 456 }];
          expectedResult = service.addPreciosToCollectionIfMissing(preciosCollection, precios);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(precios);
        });

        it('should add only unique Precios to an array', () => {
          const preciosArray: IPrecios[] = [{ id: 123 }, { id: 456 }, { id: 23465 }];
          const preciosCollection: IPrecios[] = [{ id: 123 }];
          expectedResult = service.addPreciosToCollectionIfMissing(preciosCollection, ...preciosArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const precios: IPrecios = { id: 123 };
          const precios2: IPrecios = { id: 456 };
          expectedResult = service.addPreciosToCollectionIfMissing([], precios, precios2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(precios);
          expect(expectedResult).toContain(precios2);
        });

        it('should accept null and undefined values', () => {
          const precios: IPrecios = { id: 123 };
          expectedResult = service.addPreciosToCollectionIfMissing([], null, precios, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(precios);
        });

        it('should return initial array if no Precios is added', () => {
          const preciosCollection: IPrecios[] = [{ id: 123 }];
          expectedResult = service.addPreciosToCollectionIfMissing(preciosCollection, undefined, null);
          expect(expectedResult).toEqual(preciosCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
