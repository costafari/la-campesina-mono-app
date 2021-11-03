import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAbonos, Abonos } from '../abonos.model';

import { AbonosService } from './abonos.service';

describe('Service Tests', () => {
  describe('Abonos Service', () => {
    let service: AbonosService;
    let httpMock: HttpTestingController;
    let elemDefault: IAbonos;
    let expectedResult: IAbonos | IAbonos[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AbonosService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        saldoAnterior: 0,
        abono: 0,
        nuevoSaldo: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Abonos', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Abonos()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Abonos', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            saldoAnterior: 1,
            abono: 1,
            nuevoSaldo: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Abonos', () => {
        const patchObject = Object.assign(
          {
            saldoAnterior: 1,
            abono: 1,
          },
          new Abonos()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Abonos', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            saldoAnterior: 1,
            abono: 1,
            nuevoSaldo: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Abonos', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAbonosToCollectionIfMissing', () => {
        it('should add a Abonos to an empty array', () => {
          const abonos: IAbonos = { id: 123 };
          expectedResult = service.addAbonosToCollectionIfMissing([], abonos);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(abonos);
        });

        it('should not add a Abonos to an array that contains it', () => {
          const abonos: IAbonos = { id: 123 };
          const abonosCollection: IAbonos[] = [
            {
              ...abonos,
            },
            { id: 456 },
          ];
          expectedResult = service.addAbonosToCollectionIfMissing(abonosCollection, abonos);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Abonos to an array that doesn't contain it", () => {
          const abonos: IAbonos = { id: 123 };
          const abonosCollection: IAbonos[] = [{ id: 456 }];
          expectedResult = service.addAbonosToCollectionIfMissing(abonosCollection, abonos);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(abonos);
        });

        it('should add only unique Abonos to an array', () => {
          const abonosArray: IAbonos[] = [{ id: 123 }, { id: 456 }, { id: 20677 }];
          const abonosCollection: IAbonos[] = [{ id: 123 }];
          expectedResult = service.addAbonosToCollectionIfMissing(abonosCollection, ...abonosArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const abonos: IAbonos = { id: 123 };
          const abonos2: IAbonos = { id: 456 };
          expectedResult = service.addAbonosToCollectionIfMissing([], abonos, abonos2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(abonos);
          expect(expectedResult).toContain(abonos2);
        });

        it('should accept null and undefined values', () => {
          const abonos: IAbonos = { id: 123 };
          expectedResult = service.addAbonosToCollectionIfMissing([], null, abonos, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(abonos);
        });

        it('should return initial array if no Abonos is added', () => {
          const abonosCollection: IAbonos[] = [{ id: 123 }];
          expectedResult = service.addAbonosToCollectionIfMissing(abonosCollection, undefined, null);
          expect(expectedResult).toEqual(abonosCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
