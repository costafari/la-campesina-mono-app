import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LacampesinaTestModule } from '../../../test.module';
import { AbonoFacturasDetailComponent } from 'app/entities/abono-facturas/abono-facturas-detail.component';
import { AbonoFacturas } from 'app/shared/model/abono-facturas.model';

describe('Component Tests', () => {
  describe('AbonoFacturas Management Detail Component', () => {
    let comp: AbonoFacturasDetailComponent;
    let fixture: ComponentFixture<AbonoFacturasDetailComponent>;
    const route = ({ data: of({ abonoFacturas: new AbonoFacturas(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LacampesinaTestModule],
        declarations: [AbonoFacturasDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(AbonoFacturasDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AbonoFacturasDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load abonoFacturas on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.abonoFacturas).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
