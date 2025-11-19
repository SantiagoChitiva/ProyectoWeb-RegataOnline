import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PartidaMultijugadorService } from '../../shared/partida-multijugador.service';
import { BarcoService } from '../../shared/barco.service';
import { Barco } from '../../model/barco';
import { AuthService } from '../../shared/auth.service';
import { UnirsePartidaRequest } from '../../model/partida-multijugador';

@Component({
  selector: 'app-partida-multijugador-unirse',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './partida-multijugador-unirse.component.html',
  styleUrl: './partida-multijugador-unirse.component.css'
})
export class PartidaMultijugadorUnirseComponent implements OnInit {
  barcos = signal<Barco[]>([]);
  barcoSeleccionadoId: number = 0;
  
  partidaId = signal<number>(0);
  cargando = signal(false);
  mensaje = signal('');
  
  partidaMultijugadorService = inject(PartidaMultijugadorService);
  barcoService = inject(BarcoService);
  authService = inject(AuthService);
  route = inject(ActivatedRoute);
  router = inject(Router);

  ngOnInit(): void {
    const id = +this.route.snapshot.params['id'];
    this.partidaId.set(id);
    this.cargarBarcos();
  }

  cargarBarcos(): void {
    this.barcoService.findAll().subscribe({
      next: data => this.barcos.set(data),
      error: err => console.error('Error cargando barcos', err)
    });
  }

  unirse(): void {
    if (!this.barcoSeleccionadoId) {
      this.mensaje.set('Debes seleccionar un barco');
      return;
    }

    const jugadorId = this.authService.getUserId();
    if (!jugadorId) {
      this.mensaje.set('Debes iniciar sesión para unirte a una partida');
      return;
    }

    const request: UnirsePartidaRequest = {
      jugadorId: jugadorId,
      barcoId: this.barcoSeleccionadoId
    };

    this.cargando.set(true);
    this.mensaje.set('');

    this.partidaMultijugadorService.unirseAPartida(this.partidaId(), request).subscribe({
      next: (partida) => {
        this.cargando.set(false);
        this.router.navigate(['/partida-multijugador/sala', partida.id]);
      },
      error: (err) => {
        this.cargando.set(false);
        this.mensaje.set(err.error?.message || err.error || 'Error al unirse a la partida');
        console.error('Error uniéndose a la partida', err);
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/partida-multijugador/lobby']);
  }
}
