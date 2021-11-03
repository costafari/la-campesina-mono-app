import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ClientesService } from '../service/clientes.service';

import { ClientesComponent } from './clientes.component';

describe('Component Tests', () => {
  describe('Clientes Management Component', () => {
    let comp: ClientesComponent;
    let fixture: ComponentFixture<ClientesComponent>;
    let service: ClientesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ClientesComponent],
      })
        .overrideTemplate(ClientesComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClientesComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ClientesService);

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
      expect(comp.clientes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
