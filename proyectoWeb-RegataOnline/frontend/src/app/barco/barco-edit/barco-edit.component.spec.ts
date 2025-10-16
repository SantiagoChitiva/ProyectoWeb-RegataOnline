import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BarcoEditComponent } from './barco-edit.component';

describe('BarcoEditComponent', () => {
  let component: BarcoEditComponent;
  let fixture: ComponentFixture<BarcoEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BarcoEditComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BarcoEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
