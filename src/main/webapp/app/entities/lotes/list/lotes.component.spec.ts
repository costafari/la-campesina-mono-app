import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LotesService } from '../service/lotes.service';

import { LotesComponent } from './lotes.component';

describe('Component Tests', () => {
  describe('Lotes Management Component', () => {
    let comp: LotesComponent;
    let fixture: ComponentFixture<LotesComponent>;
    let service: LotesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LotesComponent],
      })
        .overrideTemplate(LotesComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LotesComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(LotesService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.lotes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
