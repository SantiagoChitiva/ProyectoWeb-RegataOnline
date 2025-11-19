import { Component, inject, signal, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { PartidaMultijugador } from '../../model/partida-multijugador';
import { PartidaMultijugadorService } from '../../shared/partida-multijugador.service';

@Component({
  selector: 'app-partida-multijugador-lobby',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './partida-multijugador-lobby.component.html',
  styleUrl: './partida-multijugador-lobby.component.css'
})
export class PartidaMultijugadorLobbyComponent implements OnInit, OnDestroy {
  partidasDisponibles = signal<PartidaMultijugador[]>([]);
  cargando = signal(true);
  mensaje = signal('');
  
  partidaMultijugadorService = inject(PartidaMultijugadorService);
  router = inject(Router);
  
  private intervaloActualizacion: any;

  ngOnInit(): void {
    this.cargarPartidasDisponibles();
    
    // Actualizar lista cada 3 segundos
    this.intervaloActualizacion = setInterval(() => {
      this.cargarPartidasDisponibles();
    }, 3000);
  }

  ngOnDestroy(): void {
    if (this.intervaloActualizacion) {
      clearInterval(this.intervaloActualizacion);
    }
  }

  cargarPartidasDisponibles(): void {
    this.partidaMultijugadorService.listarPartidasDisponibles().subscribe({
      next: data => {
        this.partidasDisponibles.set(data);
        this.cargando.set(false);
      },
      error: err => {
        console.error('Error cargando partidas', err);
        this.mensaje.set('Error al cargar las partidas disponibles');
        this.cargando.set(false);
      }
    });
  }

  crearPartida(): void {
    this.router.navigate(['/partida-multijugador/crear']);
  }

  unirseAPartida(partidaId: number | undefined): void {
    if (!partidaId) return;
    this.router.navigate(['/partida-multijugador/unirse', partidaId]);
  }

  verSala(partidaId: number | undefined): void {
    if (!partidaId) return;
    this.router.navigate(['/partida-multijugador/sala', partidaId]);
  }

  getEstadoTexto(estado: string | undefined): string {
    switch(estado) {
      case 'esperando': return 'Esperando jugadores';
      case 'en_curso': return 'En curso';
      case 'terminada': return 'Finalizada';
      default: return estado || 'Desconocido';
    }
  }

  getEstadoClase(estado: string | undefined): string {
    switch(estado) {
      case 'esperando': return 'estado-esperando';
      case 'en_curso': return 'estado-en-curso';
      case 'terminada': return 'estado-terminada';
      default: return '';
    }
  }
}
