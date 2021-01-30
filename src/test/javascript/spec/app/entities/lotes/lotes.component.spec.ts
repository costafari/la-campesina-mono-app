import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { LacampesinaTestModule } from '../../../test.module';
import { LotesComponent } from 'app/entities/lotes/lotes.component';
import { LotesService } from 'app/entities/lotes/lotes.service';
import { Lotes } from 'app/shared/model/lotes.model';

describe('Component Tests', () => {
  describe('Lotes Management Component', () => {
    let comp: LotesComponent;
    let fixture: ComponentFixture<LotesComponent>;
    let service: LotesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LacampesinaTestModule],
        declarations: [LotesComponent],
      })
        .overrideTemplate(LotesComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LotesComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LotesService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Lotes(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.lotes && comp.lotes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
