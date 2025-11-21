import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { PartidaMultijugadorService } from '../../shared/partida-multijugador.service';
import { MapaService, Mapa } from '../../shared/mapa.service';
import { BarcoService } from '../../shared/barco.service';
import { Barco } from '../../model/barco';
import { AuthService } from '../../shared/auth.service';

@Component({
  selector: 'app-partida-multijugador-crear',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './partida-multijugador-crear.component.html',
  styleUrl: './partida-multijugador-crear.component.css'
})
export class PartidaMultijugadorCrearComponent implements OnInit {
  mapas = signal<Mapa[]>([]);
  barcos = signal<Barco[]>([]);
  
  mapaSeleccionadoId: number = 0;
  barcoSeleccionadoId: number = 0;
  
  cargando = signal(false);
  mensaje = signal('');
  
  partidaMultijugadorService = inject(PartidaMultijugadorService);
  mapaService = inject(MapaService);
  barcoService = inject(BarcoService);
  authService = inject(AuthService);
  router = inject(Router);

  ngOnInit(): void {
    this.cargarMapas();
    this.cargarBarcos();
  }

  cargarMapas(): void {
    this.mapaService.listarMapas().subscribe({
      next: data => this.mapas.set(data),
      error: err => console.error('Error cargando mapas', err)
    });
  }

  cargarBarcos(): void {
    this.barcoService.findAll().subscribe({
      next: data => this.barcos.set(data),
      error: err => console.error('Error cargando barcos', err)
    });
  }

  crearPartida(): void {
    if (!this.mapaSeleccionadoId || !this.barcoSeleccionadoId) {
      this.mensaje.set('Debes seleccionar un mapa y un barco');
      return;
    }

    const jugadorId = this.authService.getUserId();
    if (!jugadorId) {
      this.mensaje.set('Debes iniciar sesión para crear una partida');
      return;
    }

    this.cargando.set(true);
    this.mensaje.set('');

    this.partidaMultijugadorService.crearPartida(jugadorId, this.mapaSeleccionadoId, this.barcoSeleccionadoId).subscribe({
      next: (partida) => {
        this.cargando.set(false);
        this.router.navigate(['/partida-multijugador/sala', partida.id]);
      },
      error: (err) => {
        this.cargando.set(false);
        this.mensaje.set(err.error?.message || 'Error al crear la partida');
        console.error('Error creando partida', err);
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/partida-multijugador/lobby']);
  }
}
