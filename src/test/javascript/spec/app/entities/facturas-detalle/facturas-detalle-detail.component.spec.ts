import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LacampesinaTestModule } from '../../../test.module';
import { FacturasDetalleDetailComponent } from 'app/entities/facturas-detalle/facturas-detalle-detail.component';
import { FacturasDetalle } from 'app/shared/model/facturas-detalle.model';

describe('Component Tests', () => {
  describe('FacturasDetalle Management Detail Component', () => {
    let comp: FacturasDetalleDetailComponent;
    let fixture: ComponentFixture<FacturasDetalleDetailComponent>;
    const route = ({ data: of({ facturasDetalle: new FacturasDetalle(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LacampesinaTestModule],
        declarations: [FacturasDetalleDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(FacturasDetalleDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FacturasDetalleDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load facturasDetalle on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.facturasDetalle).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
