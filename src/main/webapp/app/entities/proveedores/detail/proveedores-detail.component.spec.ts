import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProveedoresDetailComponent } from './proveedores-detail.component';

describe('Component Tests', () => {
  describe('Proveedores Management Detail Component', () => {
    let comp: ProveedoresDetailComponent;
    let fixture: ComponentFixture<ProveedoresDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ProveedoresDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ proveedores: { id: 123 } }) },
          },
        ],
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
        expect(comp.proveedores).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
