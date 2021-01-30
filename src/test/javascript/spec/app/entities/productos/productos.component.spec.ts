import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { LacampesinaTestModule } from '../../../test.module';
import { ProductosComponent } from 'app/entities/productos/productos.component';
import { ProductosService } from 'app/entities/productos/productos.service';
import { Productos } from 'app/shared/model/productos.model';

describe('Component Tests', () => {
  describe('Productos Management Component', () => {
    let comp: ProductosComponent;
    let fixture: ComponentFixture<ProductosComponent>;
    let service: ProductosService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LacampesinaTestModule],
        declarations: [ProductosComponent],
      })
        .overrideTemplate(ProductosComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProductosComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProductosService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Productos(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.productos && comp.productos[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
