import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LacampesinaTestModule } from '../../../test.module';
import { PreciosDetailComponent } from 'app/entities/precios/precios-detail.component';
import { Precios } from 'app/shared/model/precios.model';

describe('Component Tests', () => {
  describe('Precios Management Detail Component', () => {
    let comp: PreciosDetailComponent;
    let fixture: ComponentFixture<PreciosDetailComponent>;
    const route = ({ data: of({ precios: new Precios(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LacampesinaTestModule],
        declarations: [PreciosDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
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
        expect(comp.precios).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
