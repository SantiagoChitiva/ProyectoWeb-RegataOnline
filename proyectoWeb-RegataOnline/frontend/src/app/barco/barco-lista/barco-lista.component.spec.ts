import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BarcoListaComponent } from './barco-lista.component';

describe('BarcoListaComponent', () => {
  let component: BarcoListaComponent;
  let fixture: ComponentFixture<BarcoListaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BarcoListaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BarcoListaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
