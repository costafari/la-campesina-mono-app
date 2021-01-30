import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LacampesinaTestModule } from '../../../test.module';
import { ProveedoresDetailComponent } from 'app/entities/proveedores/proveedores-detail.component';
import { Proveedores } from 'app/shared/model/proveedores.model';

describe('Component Tests', () => {
  describe('Proveedores Management Detail Component', () => {
    let comp: ProveedoresDetailComponent;
    let fixture: ComponentFixture<ProveedoresDetailComponent>;
    const route = ({ data: of({ proveedores: new Proveedores(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LacampesinaTestModule],
        declarations: [ProveedoresDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ProveedoresDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProveedoresDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load proveedores on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.proveedores).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
