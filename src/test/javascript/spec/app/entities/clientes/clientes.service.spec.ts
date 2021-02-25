import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ClientesService } from 'app/entities/clientes/clientes.service';
import { IClientes, Clientes } from 'app/shared/model/clientes.model';

describe('Service Tests', () => {
  describe('Clientes Service', () => {
    let injector: TestBed;
    let service: ClientesService;
    let httpMock: HttpTestingController;
    let elemDefault: IClientes;
    let expectedResult: IClientes | IClientes[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ClientesService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Clientes(
        0,
        false,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        0,
        0,
        0,
        0
      );
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

      it('should return a list of Clientes', () => {
        const returnedFromService = Object.assign(
          {
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
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
