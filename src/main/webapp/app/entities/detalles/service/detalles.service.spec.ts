import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDetalles, Detalles } from '../detalles.model';

import { DetallesService } from './detalles.service';

describe('Service Tests', () => {
  describe('Detalles Service', () => {
    let service: DetallesService;
    let httpMock: HttpTestingController;
    let elemDefault: IDetalles;
    let expectedResult: IDetalles | IDetalles[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DetallesService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        cantidad: 0,
        total: 0,
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

      it('should create a Detalles', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Detalles()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Detalles', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            cantidad: 1,
            total: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Detalles', () => {
        const patchObject = Object.assign(
          {
            cantidad: 1,
            total: 1,
          },
          new Detalles()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Detalles', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            cantidad: 1,
            total: 1,
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

      it('should delete a Detalles', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDetallesToCollectionIfMissing', () => {
        it('should add a Detalles to an empty array', () => {
          const detalles: IDetalles = { id: 123 };
          expectedResult = service.addDetallesToCollectionIfMissing([], detalles);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(detalles);
        });

        it('should not add a Detalles to an array that contains it', () => {
          const detalles: IDetalles = { id: 123 };
          const detallesCollection: IDetalles[] = [
            {
              ...detalles,
            },
            { id: 456 },
          ];
          expectedResult = service.addDetallesToCollectionIfMissing(detallesCollection, detalles);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Detalles to an array that doesn't contain it", () => {
          const detalles: IDetalles = { id: 123 };
          const detallesCollection: IDetalles[] = [{ id: 456 }];
          expectedResult = service.addDetallesToCollectionIfMissing(detallesCollection, detalles);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(detalles);
        });

        it('should add only unique Detalles to an array', () => {
          const detallesArray: IDetalles[] = [{ id: 123 }, { id: 456 }, { id: 50318 }];
          const detallesCollection: IDetalles[] = [{ id: 123 }];
          expectedResult = service.addDetallesToCollectionIfMissing(detallesCollection, ...detallesArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const detalles: IDetalles = { id: 123 };
          const detalles2: IDetalles = { id: 456 };
          expectedResult = service.addDetallesToCollectionIfMissing([], detalles, detalles2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(detalles);
          expect(expectedResult).toContain(detalles2);
        });

        it('should accept null and undefined values', () => {
          const detalles: IDetalles = { id: 123 };
          expectedResult = service.addDetallesToCollectionIfMissing([], null, detalles, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(detalles);
        });

        it('should return initial array if no Detalles is added', () => {
          const detallesCollection: IDetalles[] = [{ id: 123 }];
          expectedResult = service.addDetallesToCollectionIfMissing(detallesCollection, undefined, null);
          expect(expectedResult).toEqual(detallesCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
