import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { LacampesinaTestModule } from '../../../test.module';
import { PreciosComponent } from 'app/entities/precios/precios.component';
import { PreciosService } from 'app/entities/precios/precios.service';
import { Precios } from 'app/shared/model/precios.model';

describe('Component Tests', () => {
  describe('Precios Management Component', () => {
    let comp: PreciosComponent;
    let fixture: ComponentFixture<PreciosComponent>;
    let service: PreciosService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LacampesinaTestModule],
        declarations: [PreciosComponent],
      })
        .overrideTemplate(PreciosComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PreciosComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PreciosService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Precios(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.precios && comp.precios[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
