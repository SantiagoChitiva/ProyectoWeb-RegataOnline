import { Component, inject, output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Jugador } from '../../model/jugador';
import { JugadorService } from '../../shared/jugador.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-jugador-lista',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './jugador-lista.component.html',
  styleUrl: './jugador-lista.component.css'
})
export class JugadorListaComponent {
  jugadores = signal<Jugador[]>([]);

  jugadorClicked = output<Jugador>();
  jugadorService = inject(JugadorService);

  ngOnInit(): void {
    this.jugadorService.findAll().subscribe({
      next: data => this.jugadores.set(data),
      error: err => console.error('Error cargando jugadores', err)
    });
  }

  jugadorSelected(jugador: Jugador): void {
    this.jugadorClicked.emit(jugador);
  }
}
