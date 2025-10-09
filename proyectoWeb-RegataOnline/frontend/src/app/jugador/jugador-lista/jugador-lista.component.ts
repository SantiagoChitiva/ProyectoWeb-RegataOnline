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
    this.cargarJugadores();
  }

  cargarJugadores(): void {
    this.jugadorService.findAll().subscribe({
      next: data => this.jugadores.set(data),
      error: err => console.error('Error cargando jugadores', err)
    });
  }

  jugadorSelected(jugador: Jugador): void {
    this.jugadorClicked.emit(jugador);
  }

  eliminar(id: number | undefined, nombre: string | undefined): void {
    if (!id) return;
    
    if (confirm(`¿Estás seguro de eliminar al jugador "${nombre}"?`)) {
      this.jugadorService.delete(id).subscribe({
        next: () => {
          console.log('Jugador eliminado');
          this.cargarJugadores();
        },
        error: err => {
          alert('Error al eliminar el jugador');
          console.error(err);
        }
      });
    }
  }
}
