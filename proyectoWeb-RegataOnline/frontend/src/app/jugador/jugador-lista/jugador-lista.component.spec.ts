import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JugadorListaComponent } from './jugador-lista.component';

describe('JugadorListaComponent', () => {
  let component: JugadorListaComponent;
  let fixture: ComponentFixture<JugadorListaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JugadorListaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(JugadorListaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
