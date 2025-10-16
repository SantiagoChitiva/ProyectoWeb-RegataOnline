import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JugadorViewComponent } from './jugador-view.component';

describe('JugadorViewComponent', () => {
  let component: JugadorViewComponent;
  let fixture: ComponentFixture<JugadorViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JugadorViewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(JugadorViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
