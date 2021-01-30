import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LacampesinaTestModule } from '../../../test.module';
import { LotesDetailComponent } from 'app/entities/lotes/lotes-detail.component';
import { Lotes } from 'app/shared/model/lotes.model';

describe('Component Tests', () => {
  describe('Lotes Management Detail Component', () => {
    let comp: LotesDetailComponent;
    let fixture: ComponentFixture<LotesDetailComponent>;
    const route = ({ data: of({ lotes: new Lotes(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LacampesinaTestModule],
        declarations: [LotesDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
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
        expect(comp.lotes).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
