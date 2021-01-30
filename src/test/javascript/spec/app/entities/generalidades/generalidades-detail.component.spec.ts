import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LacampesinaTestModule } from '../../../test.module';
import { GeneralidadesDetailComponent } from 'app/entities/generalidades/generalidades-detail.component';
import { Generalidades } from 'app/shared/model/generalidades.model';

describe('Component Tests', () => {
  describe('Generalidades Management Detail Component', () => {
    let comp: GeneralidadesDetailComponent;
    let fixture: ComponentFixture<GeneralidadesDetailComponent>;
    const route = ({ data: of({ generalidades: new Generalidades(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LacampesinaTestModule],
        declarations: [GeneralidadesDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(GeneralidadesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GeneralidadesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load generalidades on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.generalidades).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
