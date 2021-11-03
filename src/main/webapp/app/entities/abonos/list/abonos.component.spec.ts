import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AbonosService } from '../service/abonos.service';

import { AbonosComponent } from './abonos.component';

describe('Component Tests', () => {
  describe('Abonos Management Component', () => {
    let comp: AbonosComponent;
    let fixture: ComponentFixture<AbonosComponent>;
    let service: AbonosService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AbonosComponent],
      })
        .overrideTemplate(AbonosComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AbonosComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AbonosService);

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
      expect(comp.abonos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
