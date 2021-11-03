import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PreciosDetailComponent } from './precios-detail.component';

describe('Component Tests', () => {
  describe('Precios Management Detail Component', () => {
    let comp: PreciosDetailComponent;
    let fixture: ComponentFixture<PreciosDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PreciosDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ precios: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PreciosDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PreciosDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load precios on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.precios).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
