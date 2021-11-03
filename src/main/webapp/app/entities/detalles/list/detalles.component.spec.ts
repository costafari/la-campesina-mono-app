import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DetallesService } from '../service/detalles.service';

import { DetallesComponent } from './detalles.component';

describe('Component Tests', () => {
  describe('Detalles Management Component', () => {
    let comp: DetallesComponent;
    let fixture: ComponentFixture<DetallesComponent>;
    let service: DetallesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DetallesComponent],
      })
        .overrideTemplate(DetallesComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DetallesComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DetallesService);

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
      expect(comp.detalles?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
