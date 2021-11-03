jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FacturasService } from '../service/facturas.service';
import { IFacturas, Facturas } from '../facturas.model';
import { IClientes } from 'app/entities/clientes/clientes.model';
import { ClientesService } from 'app/entities/clientes/service/clientes.service';

import { FacturasUpdateComponent } from './facturas-update.component';

describe('Component Tests', () => {
  describe('Facturas Management Update Component', () => {
    let comp: FacturasUpdateComponent;
    let fixture: ComponentFixture<FacturasUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let facturasService: FacturasService;
    let clientesService: ClientesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FacturasUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FacturasUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FacturasUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      facturasService = TestBed.inject(FacturasService);
      clientesService = TestBed.inject(ClientesService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Clientes query and add missing value', () => {
        const facturas: IFacturas = { id: 456 };
        const clientes: IClientes = { id: 1580 };
        facturas.clientes = clientes;

        const clientesCollection: IClientes[] = [{ id: 83291 }];
        jest.spyOn(clientesService, 'query').mockReturnValue(of(new HttpResponse({ body: clientesCollection })));
        const additionalClientes = [clientes];
        const expectedCollection: IClientes[] = [...additionalClientes, ...clientesCollection];
        jest.spyOn(clientesService, 'addClientesToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ facturas });
        comp.ngOnInit();

        expect(clientesService.query).toHaveBeenCalled();
        expect(clientesService.addClientesToCollectionIfMissing).toHaveBeenCalledWith(clientesCollection, ...additionalClientes);
        expect(comp.clientesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const facturas: IFacturas = { id: 456 };
        const clientes: IClientes = { id: 32262 };
        facturas.clientes = clientes;

        activatedRoute.data = of({ facturas });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(facturas));
        expect(comp.clientesSharedCollection).toContain(clientes);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Facturas>>();
        const facturas = { id: 123 };
        jest.spyOn(facturasService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ facturas });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: facturas }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(facturasService.update).toHaveBeenCalledWith(facturas);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Facturas>>();
        const facturas = new Facturas();
        jest.spyOn(facturasService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ facturas });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: facturas }));
        saveSubject.complete();

        // THEN
        expect(facturasService.create).toHaveBeenCalledWith(facturas);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Facturas>>();
        const facturas = { id: 123 };
        jest.spyOn(facturasService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ facturas });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(facturasService.update).toHaveBeenCalledWith(facturas);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackClientesById', () => {
        it('Should return tracked Clientes primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackClientesById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
