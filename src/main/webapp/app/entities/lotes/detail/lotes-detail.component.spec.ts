import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LotesDetailComponent } from './lotes-detail.component';

describe('Component Tests', () => {
  describe('Lotes Management Detail Component', () => {
    let comp: LotesDetailComponent;
    let fixture: ComponentFixture<LotesDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LotesDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ lotes: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LotesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LotesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load lotes on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.lotes).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
