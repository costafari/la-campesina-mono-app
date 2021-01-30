import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { LacampesinaTestModule } from '../../../test.module';
import { ClientesComponent } from 'app/entities/clientes/clientes.component';
import { ClientesService } from 'app/entities/clientes/clientes.service';
import { Clientes } from 'app/shared/model/clientes.model';

describe('Component Tests', () => {
  describe('Clientes Management Component', () => {
    let comp: ClientesComponent;
    let fixture: ComponentFixture<ClientesComponent>;
    let service: ClientesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LacampesinaTestModule],
        declarations: [ClientesComponent],
      })
        .overrideTemplate(ClientesComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClientesComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ClientesService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Clientes(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.clientes && comp.clientes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
