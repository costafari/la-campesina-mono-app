jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PreciosService } from '../service/precios.service';
import { IPrecios, Precios } from '../precios.model';

import { PreciosUpdateComponent } from './precios-update.component';

describe('Component Tests', () => {
  describe('Precios Management Update Component', () => {
    let comp: PreciosUpdateComponent;
    let fixture: ComponentFixture<PreciosUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let preciosService: PreciosService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PreciosUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PreciosUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PreciosUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      preciosService = TestBed.inject(PreciosService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const precios: IPrecios = { id: 456 };

        activatedRoute.data = of({ precios });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(precios));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Precios>>();
        const precios = { id: 123 };
        jest.spyOn(preciosService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ precios });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: precios }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(preciosService.update).toHaveBeenCalledWith(precios);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Precios>>();
        const precios = new Precios();
        jest.spyOn(preciosService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ precios });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: precios }));
        saveSubject.complete();

        // THEN
        expect(preciosService.create).toHaveBeenCalledWith(precios);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Precios>>();
        const precios = { id: 123 };
        jest.spyOn(preciosService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ precios });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(preciosService.update).toHaveBeenCalledWith(precios);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
