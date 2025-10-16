import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BarcoCreateComponent } from './barco-create.component';

describe('BarcoCreateComponent', () => {
  let component: BarcoCreateComponent;
  let fixture: ComponentFixture<BarcoCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BarcoCreateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BarcoCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
