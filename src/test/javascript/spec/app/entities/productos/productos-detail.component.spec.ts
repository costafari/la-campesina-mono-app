import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LacampesinaTestModule } from '../../../test.module';
import { ProductosDetailComponent } from 'app/entities/productos/productos-detail.component';
import { Productos } from 'app/shared/model/productos.model';

describe('Component Tests', () => {
  describe('Productos Management Detail Component', () => {
    let comp: ProductosDetailComponent;
    let fixture: ComponentFixture<ProductosDetailComponent>;
    const route = ({ data: of({ productos: new Productos(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LacampesinaTestModule],
        declarations: [ProductosDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ProductosDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProductosDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load productos on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.productos).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
