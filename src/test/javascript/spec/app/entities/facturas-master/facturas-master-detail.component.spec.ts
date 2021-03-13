import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LacampesinaTestModule } from '../../../test.module';
import { FacturasMasterDetailComponent } from 'app/entities/facturas-master/facturas-master-detail.component';
import { FacturasMaster } from 'app/shared/model/facturas-master.model';

describe('Component Tests', () => {
  describe('FacturasMaster Management Detail Component', () => {
    let comp: FacturasMasterDetailComponent;
    let fixture: ComponentFixture<FacturasMasterDetailComponent>;
    const route = ({ data: of({ facturasMaster: new FacturasMaster(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LacampesinaTestModule],
        declarations: [FacturasMasterDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(FacturasMasterDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FacturasMasterDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load facturasMaster on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.facturasMaster).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
