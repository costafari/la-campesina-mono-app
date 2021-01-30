import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { LacampesinaTestModule } from '../../../test.module';
import { TicketsComponent } from 'app/entities/tickets/tickets.component';
import { TicketsService } from 'app/entities/tickets/tickets.service';
import { Tickets } from 'app/shared/model/tickets.model';

describe('Component Tests', () => {
  describe('Tickets Management Component', () => {
    let comp: TicketsComponent;
    let fixture: ComponentFixture<TicketsComponent>;
    let service: TicketsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LacampesinaTestModule],
        declarations: [TicketsComponent],
      })
        .overrideTemplate(TicketsComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TicketsComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TicketsService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Tickets(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.tickets && comp.tickets[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
