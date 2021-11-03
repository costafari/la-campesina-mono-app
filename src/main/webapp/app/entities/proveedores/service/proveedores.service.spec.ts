import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProveedores, Proveedores } from '../proveedores.model';

import { ProveedoresService } from './proveedores.service';

describe('Service Tests', () => {
  describe('Proveedores Service', () => {
    let service: ProveedoresService;
    let httpMock: HttpTestingController;
    let elemDefault: IProveedores;
    let expectedResult: IProveedores | IProveedores[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ProveedoresService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        direccion: 'AAAAAAA',
        nombreContacto: 'AAAAAAA',
        nombreEmpresa: 'AAAAAAA',
        notas: 'AAAAAAA',
        sitioWeb: 'AAAAAAA',
        telefonoFijo: 0,
        telefonoFijo2: 0,
        telefonoMovil: 0,
        telefonoMovil2: 0,
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

      it('should create a Proveedores', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Proveedores()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Proveedores', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            direccion: 'BBBBBB',
            nombreContacto: 'BBBBBB',
            nombreEmpresa: 'BBBBBB',
            notas: 'BBBBBB',
            sitioWeb: 'BBBBBB',
            telefonoFijo: 1,
            telefonoFijo2: 1,
            telefonoMovil: 1,
            telefonoMovil2: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Proveedores', () => {
        const patchObject = Object.assign(
          {
            direccion: 'BBBBBB',
            nombreContacto: 'BBBBBB',
            nombreEmpresa: 'BBBBBB',
            notas: 'BBBBBB',
            telefonoMovil: 1,
            telefonoMovil2: 1,
          },
          new Proveedores()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Proveedores', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            direccion: 'BBBBBB',
            nombreContacto: 'BBBBBB',
            nombreEmpresa: 'BBBBBB',
            notas: 'BBBBBB',
            sitioWeb: 'BBBBBB',
            telefonoFijo: 1,
            telefonoFijo2: 1,
            telefonoMovil: 1,
            telefonoMovil2: 1,
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

      it('should delete a Proveedores', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addProveedoresToCollectionIfMissing', () => {
        it('should add a Proveedores to an empty array', () => {
          const proveedores: IProveedores = { id: 123 };
          expectedResult = service.addProveedoresToCollectionIfMissing([], proveedores);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(proveedores);
        });

        it('should not add a Proveedores to an array that contains it', () => {
          const proveedores: IProveedores = { id: 123 };
          const proveedoresCollection: IProveedores[] = [
            {
              ...proveedores,
            },
            { id: 456 },
          ];
          expectedResult = service.addProveedoresToCollectionIfMissing(proveedoresCollection, proveedores);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Proveedores to an array that doesn't contain it", () => {
          const proveedores: IProveedores = { id: 123 };
          const proveedoresCollection: IProveedores[] = [{ id: 456 }];
          expectedResult = service.addProveedoresToCollectionIfMissing(proveedoresCollection, proveedores);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(proveedores);
        });

        it('should add only unique Proveedores to an array', () => {
          const proveedoresArray: IProveedores[] = [{ id: 123 }, { id: 456 }, { id: 49151 }];
          const proveedoresCollection: IProveedores[] = [{ id: 123 }];
          expectedResult = service.addProveedoresToCollectionIfMissing(proveedoresCollection, ...proveedoresArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const proveedores: IProveedores = { id: 123 };
          const proveedores2: IProveedores = { id: 456 };
          expectedResult = service.addProveedoresToCollectionIfMissing([], proveedores, proveedores2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(proveedores);
          expect(expectedResult).toContain(proveedores2);
        });

        it('should accept null and undefined values', () => {
          const proveedores: IProveedores = { id: 123 };
          expectedResult = service.addProveedoresToCollectionIfMissing([], null, proveedores, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(proveedores);
        });

        it('should return initial array if no Proveedores is added', () => {
          const proveedoresCollection: IProveedores[] = [{ id: 123 }];
          expectedResult = service.addProveedoresToCollectionIfMissing(proveedoresCollection, undefined, null);
          expect(expectedResult).toEqual(proveedoresCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
