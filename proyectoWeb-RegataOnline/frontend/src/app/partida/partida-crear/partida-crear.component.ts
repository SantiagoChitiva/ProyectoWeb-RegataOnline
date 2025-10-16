import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PartidaService, CrearPartidaRequest } from '../../shared/partida.service';
import { MapaService, Mapa } from '../../shared/mapa.service';
import { BarcoService } from '../../shared/barco.service';
import { JugadorService } from '../../shared/jugador.service';
import { Barco } from '../../model/barco';
import { Jugador } from '../../model/jugador';

@Component({
  selector: 'app-partida-crear',
  imports: [CommonModule, FormsModule],
  templateUrl: './partida-crear.component.html',
  styleUrl: './partida-crear.component.css'
})
export class PartidaCrearComponent {
  partidaService = inject(PartidaService);
  mapaService = inject(MapaService);
  barcoService = inject(BarcoService);
  jugadorService = inject(JugadorService);
  router = inject(Router);
  route = inject(ActivatedRoute);

  jugadorId = signal<number>(0);
  jugador = signal<Jugador | null>(null);
  mapas = signal<Mapa[]>([]);
  barcos = signal<Barco[]>([]);
  
  mapaSeleccionado = signal<number | null>(null);
  barcoSeleccionado = signal<number | null>(null);

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const jugadorId = Number(params['jugadorId']);
      if (jugadorId) {
        this.jugadorId.set(jugadorId);
        this.cargarJugador(jugadorId);
        this.cargarMapas();
        this.cargarBarcos(jugadorId);
      } else {
        alert('No se especificó un jugador');
        this.router.navigate(['/partida/menu']);
      }
    });
  }

  cargarJugador(id: number): void {
    this.jugadorService.findById(id).subscribe({
      next: jugador => this.jugador.set(jugador),
      error: err => console.error('Error cargando jugador', err)
    });
  }

  cargarMapas(): void {
    this.mapaService.listarMapas().subscribe({
      next: data => this.mapas.set(data),
      error: err => console.error('Error cargando mapas', err)
    });
  }

  cargarBarcos(jugadorId: number): void {
    this.barcoService.findAll().subscribe({
      next: data => {
        // Filtrar barcos del jugador
        const barcosJugador = data.filter(b => b.jugadorId === jugadorId);
        this.barcos.set(barcosJugador);
      },
      error: err => console.error('Error cargando barcos', err)
    });
  }

  crearPartida(): void {
    if (!this.mapaSeleccionado() || !this.barcoSeleccionado()) {
      alert('Por favor selecciona un mapa y un barco');
      return;
    }

    const request: CrearPartidaRequest = {
      jugadorId: this.jugadorId(),
      mapaId: this.mapaSeleccionado()!,
      barcoId: this.barcoSeleccionado()!
    };

    this.partidaService.crearPartida(request).subscribe({
      next: partida => {
        alert('¡Partida creada exitosamente!');
        this.router.navigate(['/partida/juego', partida.id]);
      },
      error: err => {
        console.error('Error al crear partida', err);
        const mensaje = err.error || 'Error al crear la partida';
        alert(mensaje);
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/partida/menu']);
  }
}
