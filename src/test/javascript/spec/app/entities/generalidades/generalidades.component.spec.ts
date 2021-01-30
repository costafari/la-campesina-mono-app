import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { LacampesinaTestModule } from '../../../test.module';
import { GeneralidadesComponent } from 'app/entities/generalidades/generalidades.component';
import { GeneralidadesService } from 'app/entities/generalidades/generalidades.service';
import { Generalidades } from 'app/shared/model/generalidades.model';

describe('Component Tests', () => {
  describe('Generalidades Management Component', () => {
    let comp: GeneralidadesComponent;
    let fixture: ComponentFixture<GeneralidadesComponent>;
    let service: GeneralidadesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LacampesinaTestModule],
        declarations: [GeneralidadesComponent],
      })
        .overrideTemplate(GeneralidadesComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GeneralidadesComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GeneralidadesService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Generalidades(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.generalidades && comp.generalidades[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
