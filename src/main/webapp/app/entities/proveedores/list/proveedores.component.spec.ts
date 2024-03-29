import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ProveedoresService } from '../service/proveedores.service';

import { ProveedoresComponent } from './proveedores.component';

describe('Component Tests', () => {
  describe('Proveedores Management Component', () => {
    let comp: ProveedoresComponent;
    let fixture: ComponentFixture<ProveedoresComponent>;
    let service: ProveedoresService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProveedoresComponent],
      })
        .overrideTemplate(ProveedoresComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProveedoresComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ProveedoresService);

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
      expect(comp.proveedores?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
