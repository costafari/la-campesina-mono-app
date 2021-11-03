import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AbonosDetailComponent } from './abonos-detail.component';

describe('Component Tests', () => {
  describe('Abonos Management Detail Component', () => {
    let comp: AbonosDetailComponent;
    let fixture: ComponentFixture<AbonosDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AbonosDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ abonos: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AbonosDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AbonosDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load abonos on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.abonos).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
