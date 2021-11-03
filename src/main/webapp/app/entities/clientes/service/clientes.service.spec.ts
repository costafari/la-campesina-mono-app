import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IClientes, Clientes } from '../clientes.model';

import { ClientesService } from './clientes.service';

describe('Service Tests', () => {
  describe('Clientes Service', () => {
    let service: ClientesService;
    let httpMock: HttpTestingController;
    let elemDefault: IClientes;
    let expectedResult: IClientes | IClientes[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ClientesService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        activo: false,
        apellidos: 'AAAAAAA',
        direcion: 'AAAAAAA',
        email: 'AAAAAAA',
        nombreContacto: 'AAAAAAA',
        nombreEmpresa: 'AAAAAAA',
        nombres: 'AAAAAAA',
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

      it('should create a Clientes', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Clientes()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Clientes', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            activo: true,
            apellidos: 'BBBBBB',
            direcion: 'BBBBBB',
            email: 'BBBBBB',
            nombreContacto: 'BBBBBB',
            nombreEmpresa: 'BBBBBB',
            nombres: 'BBBBBB',
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

      it('should partial update a Clientes', () => {
        const patchObject = Object.assign(
          {
            activo: true,
            apellidos: 'BBBBBB',
            direcion: 'BBBBBB',
            email: 'BBBBBB',
            sitioWeb: 'BBBBBB',
            telefonoMovil2: 1,
          },
          new Clientes()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Clientes', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            activo: true,
            apellidos: 'BBBBBB',
            direcion: 'BBBBBB',
            email: 'BBBBBB',
            nombreContacto: 'BBBBBB',
            nombreEmpresa: 'BBBBBB',
            nombres: 'BBBBBB',
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

      it('should delete a Clientes', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addClientesToCollectionIfMissing', () => {
        it('should add a Clientes to an empty array', () => {
          const clientes: IClientes = { id: 123 };
          expectedResult = service.addClientesToCollectionIfMissing([], clientes);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(clientes);
        });

        it('should not add a Clientes to an array that contains it', () => {
          const clientes: IClientes = { id: 123 };
          const clientesCollection: IClientes[] = [
            {
              ...clientes,
            },
            { id: 456 },
          ];
          expectedResult = service.addClientesToCollectionIfMissing(clientesCollection, clientes);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Clientes to an array that doesn't contain it", () => {
          const clientes: IClientes = { id: 123 };
          const clientesCollection: IClientes[] = [{ id: 456 }];
          expectedResult = service.addClientesToCollectionIfMissing(clientesCollection, clientes);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(clientes);
        });

        it('should add only unique Clientes to an array', () => {
          const clientesArray: IClientes[] = [{ id: 123 }, { id: 456 }, { id: 3428 }];
          const clientesCollection: IClientes[] = [{ id: 123 }];
          expectedResult = service.addClientesToCollectionIfMissing(clientesCollection, ...clientesArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const clientes: IClientes = { id: 123 };
          const clientes2: IClientes = { id: 456 };
          expectedResult = service.addClientesToCollectionIfMissing([], clientes, clientes2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(clientes);
          expect(expectedResult).toContain(clientes2);
        });

        it('should accept null and undefined values', () => {
          const clientes: IClientes = { id: 123 };
          expectedResult = service.addClientesToCollectionIfMissing([], null, clientes, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(clientes);
        });

        it('should return initial array if no Clientes is added', () => {
          const clientesCollection: IClientes[] = [{ id: 123 }];
          expectedResult = service.addClientesToCollectionIfMissing(clientesCollection, undefined, null);
          expect(expectedResult).toEqual(clientesCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
