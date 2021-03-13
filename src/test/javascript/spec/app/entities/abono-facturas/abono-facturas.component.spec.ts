import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { LacampesinaTestModule } from '../../../test.module';
import { AbonoFacturasComponent } from 'app/entities/abono-facturas/abono-facturas.component';
import { AbonoFacturasService } from 'app/entities/abono-facturas/abono-facturas.service';
import { AbonoFacturas } from 'app/shared/model/abono-facturas.model';

describe('Component Tests', () => {
  describe('AbonoFacturas Management Component', () => {
    let comp: AbonoFacturasComponent;
    let fixture: ComponentFixture<AbonoFacturasComponent>;
    let service: AbonoFacturasService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LacampesinaTestModule],
        declarations: [AbonoFacturasComponent],
      })
        .overrideTemplate(AbonoFacturasComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AbonoFacturasComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AbonoFacturasService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new AbonoFacturas(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.abonoFacturas && comp.abonoFacturas[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
