import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { LacampesinaTestModule } from '../../../test.module';
import { FacturasMasterComponent } from 'app/entities/facturas-master/facturas-master.component';
import { FacturasMasterService } from 'app/entities/facturas-master/facturas-master.service';
import { FacturasMaster } from 'app/shared/model/facturas-master.model';

describe('Component Tests', () => {
  describe('FacturasMaster Management Component', () => {
    let comp: FacturasMasterComponent;
    let fixture: ComponentFixture<FacturasMasterComponent>;
    let service: FacturasMasterService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LacampesinaTestModule],
        declarations: [FacturasMasterComponent],
      })
        .overrideTemplate(FacturasMasterComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FacturasMasterComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FacturasMasterService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new FacturasMaster(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.facturasMasters && comp.facturasMasters[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
