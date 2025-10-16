import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BarcoViewComponent } from './barco-view.component';

describe('BarcoViewComponent', () => {
  let component: BarcoViewComponent;
  let fixture: ComponentFixture<BarcoViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BarcoViewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BarcoViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
