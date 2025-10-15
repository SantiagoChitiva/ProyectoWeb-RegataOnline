import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PartidaService, Partida } from '../../shared/partida.service';
import { JugadorService } from '../../shared/jugador.service';
import { Jugador } from '../../model/jugador';

@Component({
  selector: 'app-partida-menu',
  imports: [CommonModule, FormsModule],
  templateUrl: './partida-menu.component.html',
  styleUrl: './partida-menu.component.css'
})
export class PartidaMenuComponent {
  partidaService = inject(PartidaService);
  jugadorService = inject(JugadorService);
  router = inject(Router);

  jugadores = signal<Jugador[]>([]);
  jugadorSeleccionadoId = signal<number | null>(null);
  partidaActiva = signal<Partida | null>(null);
  cargando = signal<boolean>(false);
  mensaje = signal<string>('');

  ngOnInit(): void {
    this.cargarJugadores();
  }

  cargarJugadores(): void {
    this.jugadorService.findAll().subscribe({
      next: data => this.jugadores.set(data),
      error: err => console.error('Error cargando jugadores', err)
    });
  }

  onJugadorSeleccionado(event: Event): void {
    const select = event.target as HTMLSelectElement;
    const jugadorId = Number(select.value);
    
    if (!jugadorId) {
      this.jugadorSeleccionadoId.set(null);
      this.partidaActiva.set(null);
      this.mensaje.set('');
      return;
    }

    this.jugadorSeleccionadoId.set(jugadorId);
    this.buscarPartidaActiva(jugadorId);
  }

  buscarPartidaActiva(jugadorId: number): void {
    this.cargando.set(true);
    this.mensaje.set('');
    
    this.partidaService.obtenerPartidaActiva(jugadorId).subscribe({
      next: partida => {
        this.partidaActiva.set(partida);
        this.mensaje.set('');
        this.cargando.set(false);
      },
      error: () => {
        // No hay partida activa, esto es normal
        this.partidaActiva.set(null);
        this.mensaje.set('');
        this.cargando.set(false);
      }
    });
  }

  iniciarNuevaPartida(): void {
    if (!this.jugadorSeleccionadoId()) {
      alert('Por favor selecciona un jugador');
      return;
    }
    
    this.router.navigate(['/partida/crear'], {
      queryParams: { jugadorId: this.jugadorSeleccionadoId() }
    });
  }

  continuarPartida(): void {
    const partida = this.partidaActiva();
    if (!partida || !partida.id) {
      alert('No hay partida para continuar');
      return;
    }
    
    this.router.navigate(['/partida/juego', partida.id]);
  }

  verDetallesPartida(): void {
    const partida = this.partidaActiva();
    if (!partida) return;

    alert(`
📊 Detalles de la Partida:
━━━━━━━━━━━━━━━━━━━━━
🎮 Estado: ${partida.estado}
🗺️ Mapa: ${partida.mapaFilas}x${partida.mapaColumnas}
🚢 Barco: ${partida.barcoNombre}
📍 Posición: (${partida.barcoPosicionX}, ${partida.barcoPosicionY})
🔢 Movimientos: ${partida.movimientos}
📅 Última jugada: ${partida.fechaUltimaJugada ? new Date(partida.fechaUltimaJugada).toLocaleString() : 'N/A'}
    `);
  }

  finalizarPartida(): void {
    const partida = this.partidaActiva();
    if (!partida || !partida.id) return;

    if (!confirm('¿Estás seguro de finalizar esta partida? Esta acción no se puede deshacer.')) {
      return;
    }

    this.partidaService.finalizarPartida(partida.id).subscribe({
      next: () => {
        alert('Partida finalizada exitosamente');
        this.partidaActiva.set(null);
      },
      error: err => {
        console.error('Error al finalizar partida', err);
        alert('Error al finalizar la partida');
      }
    });
  }
}
