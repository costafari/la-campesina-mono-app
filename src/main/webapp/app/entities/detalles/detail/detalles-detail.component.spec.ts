import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DetallesDetailComponent } from './detalles-detail.component';

describe('Component Tests', () => {
  describe('Detalles Management Detail Component', () => {
    let comp: DetallesDetailComponent;
    let fixture: ComponentFixture<DetallesDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DetallesDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ detalles: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DetallesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DetallesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load detalles on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.detalles).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
