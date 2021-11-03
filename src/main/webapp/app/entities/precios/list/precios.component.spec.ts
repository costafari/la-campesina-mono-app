import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PreciosService } from '../service/precios.service';

import { PreciosComponent } from './precios.component';

describe('Component Tests', () => {
  describe('Precios Management Component', () => {
    let comp: PreciosComponent;
    let fixture: ComponentFixture<PreciosComponent>;
    let service: PreciosService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PreciosComponent],
      })
        .overrideTemplate(PreciosComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PreciosComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PreciosService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.precios?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
