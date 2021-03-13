import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { LacampesinaTestModule } from '../../../test.module';
import { FacturasDetalleComponent } from 'app/entities/facturas-detalle/facturas-detalle.component';
import { FacturasDetalleService } from 'app/entities/facturas-detalle/facturas-detalle.service';
import { FacturasDetalle } from 'app/shared/model/facturas-detalle.model';

describe('Component Tests', () => {
  describe('FacturasDetalle Management Component', () => {
    let comp: FacturasDetalleComponent;
    let fixture: ComponentFixture<FacturasDetalleComponent>;
    let service: FacturasDetalleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LacampesinaTestModule],
        declarations: [FacturasDetalleComponent],
      })
        .overrideTemplate(FacturasDetalleComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FacturasDetalleComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FacturasDetalleService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new FacturasDetalle(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.facturasDetalles && comp.facturasDetalles[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
