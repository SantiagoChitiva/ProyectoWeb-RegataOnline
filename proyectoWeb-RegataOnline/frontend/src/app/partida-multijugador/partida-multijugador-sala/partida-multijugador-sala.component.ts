import { Component, inject, signal, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { PartidaMultijugador, PartidaJugador, RealizarMovimientoRequest } from '../../model/partida-multijugador';
import { PartidaMultijugadorService } from '../../shared/partida-multijugador.service';
import { MapaService, Celda, Mapa } from '../../shared/mapa.service';
import { AuthService } from '../../shared/auth.service';

@Component({
  selector: 'app-partida-multijugador-sala',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './partida-multijugador-sala.component.html',
  styleUrl: './partida-multijugador-sala.component.css'
})
export class PartidaMultijugadorSalaComponent implements OnInit, OnDestroy {
  partida = signal<PartidaMultijugador | null>(null);
  mapa = signal<Mapa | null>(null);
  matrizCeldas = signal<Celda[][]>([]);
  celdasDestinoPosibles = signal<{x: number, y: number, aceleracionX: number, aceleracionY: number}[]>([]);
  
  jugadorActualId = signal<number | null>(null);
  miPartidaJugador = signal<PartidaJugador | null>(null);
  
  cargando = signal(true);
  mensaje = signal('');
  moviendo = signal(false);
  
  private intervaloActualizacion: any;
  
  partidaMultijugadorService = inject(PartidaMultijugadorService);
  mapaService = inject(MapaService);
  authService = inject(AuthService);
  route = inject(ActivatedRoute);
  router = inject(Router);

  ngOnInit(): void {
    const partidaId = +this.route.snapshot.params['id'];
    this.jugadorActualId.set(this.authService.getUserId());
    this.cargarPartida(partidaId);
    
    // Actualizar estado cada 2 segundos
    this.intervaloActualizacion = setInterval(() => {
      this.actualizarPartida(partidaId);
    }, 2000);
  }

  ngOnDestroy(): void {
    if (this.intervaloActualizacion) {
      clearInterval(this.intervaloActualizacion);
    }
  }

  cargarPartida(id: number) {
    this.cargando.set(true);
    this.partidaMultijugadorService.obtenerPartida(id).subscribe({
      next: (partida) => {
        this.partida.set(partida);
        this.actualizarMiPartidaJugador(partida);
        
        // Verificar si la partida ya terminó
        if (partida.estado === 'terminada') {
          if (this.miPartidaJugador()?.haLlegadoMeta) {
            this.mensaje.set(`¡Felicidades! Llegaste en la posición ${this.miPartidaJugador()?.posicionFinal} 🎉`);
          } else {
            this.mensaje.set(`Partida terminada. Ganador: ${partida.ganadorNombre}`);
          }
        }
        
        // Cargar el mapa si está disponible
        if (partida.mapaId) {
          this.mapaService.buscarMapa(partida.mapaId).subscribe({
            next: (mapa) => {
              this.mapa.set(mapa);
              this.construirMatriz(mapa);
              if (partida.estado === 'en_curso') {
                this.calcularDestinosPosibles();
              }
              this.cargando.set(false);
            },
            error: (err) => {
              console.error('Error cargando mapa', err);
              this.mensaje.set('Error al cargar el mapa');
              this.cargando.set(false);
            }
          });
        } else {
          this.cargando.set(false);
        }
      },
      error: (error) => {
        console.error('Error al cargar partida:', error);
        this.mensaje.set('Error al cargar la partida');
        this.cargando.set(false);
      }
    });
  }

  actualizarEstadoPartida() {
    const partidaId = this.partida()?.id;
    if (!partidaId) return;

    this.partidaMultijugadorService.obtenerPartida(partidaId).subscribe({
      next: (partida) => {
        this.partida.set(partida);
        this.actualizarMiPartidaJugador(partida);
        
        // Verificar si la partida terminó
        if (partida.estado === 'terminada') {
          clearInterval(this.intervaloActualizacion);
          if (this.miPartidaJugador()?.haLlegadoMeta) {
            this.mensaje.set(`¡Felicidades! Llegaste en la posición ${this.miPartidaJugador()?.posicionFinal} 🎉`);
          } else {
            this.mensaje.set(`Partida terminada. Ganador: ${partida.ganadorNombre}`);
          }
        }
      },
      error: (err) => {
        console.error('Error actualizando estado partida', err);
      }
    });
  }

  actualizarPartida(id: number) {
    // Actualización silenciosa sin mostrar loading
    this.partidaMultijugadorService.obtenerPartida(id).subscribe({
      next: (partida) => {
        this.partida.set(partida);
        this.actualizarMiPartidaJugador(partida);
        
        if (partida.estado === 'en_curso') {
          this.calcularDestinosPosibles();
        }
        
        if (partida.estado === 'terminada' && this.intervaloActualizacion) {
          clearInterval(this.intervaloActualizacion);
          if (this.miPartidaJugador()?.haLlegadoMeta) {
            this.mensaje.set(`¡Felicidades! Llegaste en la posición ${this.miPartidaJugador()?.posicionFinal} 🎉`);
          } else {
            this.mensaje.set(`Partida terminada. Ganador: ${partida.ganadorNombre}`);
          }
        }
      },
      error: (err) => {
        console.error('Error actualizando partida', err);
      }
    });
  }

  actualizarMiPartidaJugador(partida: PartidaMultijugador) {
    const jugadorId = this.jugadorActualId();
    if (jugadorId && partida.jugadores) {
      const miJugador = partida.jugadores.find(j => j.jugadorId === jugadorId);
      this.miPartidaJugador.set(miJugador || null);
    }
  }

  iniciarPartida(): void {
    const partidaId = this.partida()?.id;
    if (!partidaId) return;

    this.cargando.set(true);
    this.partidaMultijugadorService.iniciarPartida(partidaId).subscribe({
      next: (partida) => {
        this.partida.set(partida);
        this.cargando.set(false);
        this.mensaje.set('¡Partida iniciada! Comienza el juego.');
      },
      error: (err) => {
        this.cargando.set(false);
        this.mensaje.set(err.error?.message || err.error || 'Error al iniciar la partida');
        console.error('Error iniciando partida', err);
      }
    });
  }

  construirMatriz(mapa: Mapa) {
    if (!mapa.celdas) return;

    const matriz: Celda[][] = [];
    
    // i = fila (posicionY), j = columna (posicionX)
    for (let i = 0; i < mapa.filas; i++) {
      const fila: Celda[] = [];
      for (let j = 0; j < mapa.columnas; j++) {
        fila.push({
          posicionX: j,
          posicionY: i,
          tipo: ''
        });
      }
      matriz.push(fila);
    }

    // Llenar con las celdas del mapa
    mapa.celdas.forEach(celda => {
      if (celda.posicionY < mapa.filas && celda.posicionX < mapa.columnas) {
        matriz[celda.posicionY][celda.posicionX] = celda;
      }
    });

    this.matrizCeldas.set(matriz);
  }

  calcularDestinosPosibles() {
    const miJugador = this.miPartidaJugador();
    if (!miJugador) return;

    const destinos: {x: number, y: number, aceleracionX: number, aceleracionY: number}[] = [];
    const velocidadX = miJugador.velocidadX || 0;
    const velocidadY = miJugador.velocidadY || 0;

    // Calcular los 9 posibles destinos (aceleraciones: -1, 0, +1 en X e Y)
    for (let acX = -1; acX <= 1; acX++) {
      for (let acY = -1; acY <= 1; acY++) {
        const nuevaVelocidadX = velocidadX + acX;
        const nuevaVelocidadY = velocidadY + acY;
        const nuevaPosX = (miJugador.posicionX || 0) + nuevaVelocidadX;
        const nuevaPosY = (miJugador.posicionY || 0) + nuevaVelocidadY;

        destinos.push({
          x: nuevaPosX,
          y: nuevaPosY,
          aceleracionX: acX,
          aceleracionY: acY
        });
      }
    }

    this.celdasDestinoPosibles.set(destinos);
  }

  esMiTurno(): boolean {
    const partida = this.partida();
    const miJugador = this.miPartidaJugador();
    
    if (!partida || !miJugador) return false;
    
    return partida.ordenTurnoActual === miJugador.ordenTurno && 
           partida.estado === 'en_curso';
  }

  esCreador(): boolean {
    const partida = this.partida();
    const jugadorId = this.jugadorActualId();
    
    return partida?.jugadorCreadorId === jugadorId;
  }

  puedeIniciar(): boolean {
    const partida = this.partida();
    return this.esCreador() && 
           partida?.estado === 'esperando' && 
           (partida?.cantidadJugadores || 0) >= 2;
  }

  moverBarco(destino: {x: number, y: number, aceleracionX: number, aceleracionY: number}): void {
    if (!this.esMiTurno() || this.moviendo()) return;

    const partidaId = this.partida()?.id;
    const jugadorId = this.jugadorActualId();
    
    if (!partidaId || !jugadorId) return;

    const request: RealizarMovimientoRequest = {
      jugadorId: jugadorId,
      aceleracionX: destino.aceleracionX,
      aceleracionY: destino.aceleracionY
    };

    this.moviendo.set(true);
    this.mensaje.set('');

    this.partidaMultijugadorService.realizarMovimiento(partidaId, request).subscribe({
      next: (partida) => {
        this.partida.set(partida);
        this.actualizarMiPartidaJugador(partida);
        this.calcularDestinosPosibles();
        this.moviendo.set(false);
      },
      error: (err) => {
        this.moviendo.set(false);
        this.mensaje.set(err.error?.message || err.error || 'Error al realizar el movimiento');
        console.error('Error moviendo barco', err);
      }
    });
  }

  esDestinoValido(x: number, y: number): boolean {
    return this.celdasDestinoPosibles().some(d => d.x === x && d.y === y);
  }

  obtenerDestino(x: number, y: number): {x: number, y: number, aceleracionX: number, aceleracionY: number} | undefined {
    return this.celdasDestinoPosibles().find(d => d.x === x && d.y === y);
  }

  obtenerColorCelda(celda: Celda): string {
    switch (celda?.tipo) {
      case 'x':
        return '#2c3e50'; // Pared
      case 'P':
        return '#27ae60'; // Partida / punto de salida
      case 'M':
        return '#f39c12'; // Meta
      default:
        return '#ecf0f1'; // Agua navegable
    }
  }

  getClaseCelda(celda: Celda): string {
    const partida = this.partida();
    if (!partida) return '';

    // Verificar si hay algún barco en esta celda
    const jugadorEnCelda = partida.jugadores?.find(
      j => j.posicionX === celda.posicionX && j.posicionY === celda.posicionY
    );

    if (jugadorEnCelda) {
      const esJugadorActual = jugadorEnCelda.jugadorId === this.jugadorActualId();
      return esJugadorActual ? 'barco-propio' : 'barco-enemigo';
    }

    return '';
  }

  getTurnoActualNombre(): string {
    const partida = this.partida();
    if (!partida || !partida.jugadores) return '';

    const jugadorTurno = partida.jugadores.find(j => j.ordenTurno === partida.ordenTurnoActual);
    return jugadorTurno?.jugadorNombre || '';
  }

  volverALobby(): void {
    this.router.navigate(['/partida-multijugador/lobby']);
  }

  getEstadoTexto(): string {
    const estado = this.partida()?.estado;
    switch(estado) {
      case 'esperando': return 'Esperando jugadores';
      case 'en_curso': return 'En curso';
      case 'terminada': return 'Finalizada';
      default: return '';
    }
  }
}
