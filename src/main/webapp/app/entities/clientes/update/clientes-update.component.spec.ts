jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClientesService } from '../service/clientes.service';
import { IClientes, Clientes } from '../clientes.model';

import { ClientesUpdateComponent } from './clientes-update.component';

describe('Component Tests', () => {
  describe('Clientes Management Update Component', () => {
    let comp: ClientesUpdateComponent;
    let fixture: ComponentFixture<ClientesUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let clientesService: ClientesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ClientesUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ClientesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClientesUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      clientesService = TestBed.inject(ClientesService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const clientes: IClientes = { id: 456 };

        activatedRoute.data = of({ clientes });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(clientes));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Clientes>>();
        const clientes = { id: 123 };
        jest.spyOn(clientesService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ clientes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: clientes }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(clientesService.update).toHaveBeenCalledWith(clientes);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Clientes>>();
        const clientes = new Clientes();
        jest.spyOn(clientesService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ clientes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: clientes }));
        saveSubject.complete();

        // THEN
        expect(clientesService.create).toHaveBeenCalledWith(clientes);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Clientes>>();
        const clientes = { id: 123 };
        jest.spyOn(clientesService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ clientes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(clientesService.update).toHaveBeenCalledWith(clientes);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
