import { Component, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { PartidaService, Partida } from '../../shared/partida.service';
import { MapaService, Celda, Mapa } from '../../shared/mapa.service';

@Component({
  selector: 'app-partida-juego',
  imports: [CommonModule],
  templateUrl: './partida-juego.component.html',
  styleUrl: './partida-juego.component.css'
})
export class PartidaJuegoComponent implements OnInit {
  partidaService = inject(PartidaService);
  mapaService = inject(MapaService);
  route = inject(ActivatedRoute);
  router = inject(Router);

  partida = signal<Partida | null>(null);
  mapa = signal<Mapa | null>(null);
  matrizCeldas = signal<Celda[][]>([]);
  celdasDestinoPosibles = signal<{x: number, y: number, aceleracionX: number, aceleracionY: number}[]>([]);
  
  cargando = signal(true);
  mensaje = signal('');
  moviendo = signal(false);

  ngOnInit(): void {
    const partidaId = +this.route.snapshot.params['id'];
    this.cargarPartida(partidaId);
  }

  cargarPartida(id: number) {
    this.cargando.set(true);
    this.partidaService.obtenerPartida(id).subscribe({
      next: (partida) => {
        this.partida.set(partida);
        
        // Verificar si la partida ya terminó
        if (partida.estado === 'terminada') {
          if (partida.haLlegadoMeta) {
            this.mensaje.set('¡Felicidades! Has llegado a la meta 🎉');
          } else {
            this.mensaje.set('Esta partida ya ha terminado');
          }
        }
        
        // Cargar el mapa
        this.mapaService.buscarMapa(partida.mapaId).subscribe({
          next: (mapa) => {
            this.mapa.set(mapa);
            this.construirMatriz(mapa);
            this.calcularDestinosPosibles();
            this.cargando.set(false);
          },
          error: (err) => {
            console.error('Error cargando mapa', err);
            this.mensaje.set('Error al cargar el mapa');
            this.cargando.set(false);
          }
        });
      },
      error: (err) => {
        console.error('Error cargando partida', err);
        this.mensaje.set('Error al cargar la partida');
        this.cargando.set(false);
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
    const partida = this.partida();
    if (!partida) return;

    const velocidadX = partida.barcoVelocidadX || 0;
    const velocidadY = partida.barcoVelocidadY || 0;
    const posX = partida.barcoPosicionX || 0;
    const posY = partida.barcoPosicionY || 0;

    const destinos: {x: number, y: number, aceleracionX: number, aceleracionY: number}[] = [];

    // Calcular las 9 opciones de aceleración
    for (let ax = -1; ax <= 1; ax++) {
      for (let ay = -1; ay <= 1; ay++) {
        const nuevaVx = velocidadX + ax;
        const nuevaVy = velocidadY + ay;
        const destinoX = posX + nuevaVx;
        const destinoY = posY + nuevaVy;

        destinos.push({
          x: destinoX,
          y: destinoY,
          aceleracionX: ax,
          aceleracionY: ay
        });
      }
    }

    this.celdasDestinoPosibles.set(destinos);
  }

  moverBarco(aceleracionX: number, aceleracionY: number) {
    const partida = this.partida();
    if (!partida || !partida.id || partida.estado !== 'activa') {
      this.mensaje.set('No se puede mover el barco en esta partida');
      return;
    }

    this.moviendo.set(true);
    this.mensaje.set('');

    this.partidaService.moverBarco(partida.id, aceleracionX, aceleracionY).subscribe({
      next: (partidaActualizada) => {
        this.partida.set(partidaActualizada);
        this.calcularDestinosPosibles();
        this.moviendo.set(false);

        // Verificar si llegó a la meta
        if (partidaActualizada.haLlegadoMeta) {
          this.mensaje.set('¡Felicidades! Has llegado a la meta en ' + 
            partidaActualizada.movimientos + ' movimientos 🎉');
        }
      },
      error: (err) => {
        console.error('=== ERROR AL MOVER ===');
        console.error('Error completo:', err);
        console.error('err.error:', err.error);
        console.error('err.error type:', typeof err.error);
        console.error('err.message:', err.message);
        console.error('err.status:', err.status);
        
        let errorMsg = 'Error desconocido';
        if (typeof err.error === 'string') {
          errorMsg = err.error;
        } else if (err.error?.message) {
          errorMsg = err.error.message;
        } else if (err.message) {
          errorMsg = err.message;
        }
        
        this.mensaje.set(errorMsg);
        this.moviendo.set(false);
        alert('❌ Error del backend: ' + errorMsg);
      }
    });
  }

  clickCelda(x: number, y: number) {
    // Solo permitir click si es un destino posible y válido
    if (!this.esDestinoPosible(x, y) || !this.esDestinoValido(x, y)) {
      return;
    }

    const partida = this.partida();
    console.log('=== CLICK EN CELDA ===');
    console.log('Celda clickeada: (', x, ',', y, ')');
    console.log('Posición actual barco: (', partida?.barcoPosicionX, ',', partida?.barcoPosicionY, ')');
    console.log('Velocidad actual: (', partida?.barcoVelocidadX, ',', partida?.barcoVelocidadY, ')');

    // Buscar la aceleración correspondiente a este destino
    const destino = this.celdasDestinoPosibles().find(d => d.x === x && d.y === y);
    if (destino) {
      console.log('Aceleración a aplicar: (', destino.aceleracionX, ',', destino.aceleracionY, ')');
      console.log('Destino calculado: (', destino.x, ',', destino.y, ')');
      this.moverBarco(destino.aceleracionX, destino.aceleracionY);
    } else {
      console.error('❌ No se encontró destino para (', x, ',', y, ')');
    }
  }

  esBarco(x: number, y: number): boolean {
    const partida = this.partida();
    return partida?.barcoPosicionX === x && partida?.barcoPosicionY === y;
  }

  esDestinoValido(x: number, y: number): boolean {
    const mapa = this.mapa();
    if (!mapa) return false;

    // Verificar límites
    if (x < 0 || x >= mapa.columnas || y < 0 || y >= mapa.filas) return false;

    // Verificar que no sea pared
    const matriz = this.matrizCeldas();
    const celda = matriz[y]?.[x];
    return celda?.tipo !== 'x';
  }

  esDestinoPosible(x: number, y: number): boolean {
    return this.celdasDestinoPosibles().some(d => d.x === x && d.y === y);
  }

  obtenerColorCelda(celda: Celda, x: number, y: number): string {
    // Barco
    if (this.esBarco(x, y)) return '#e74c3c'; // Rojo brillante
    
    // Destino posible
    if (this.esDestinoPosible(x, y) && this.esDestinoValido(x, y)) {
      return '#3498db80'; // Azul transparente
    }

    // Tipos de celda
    switch(celda.tipo) {
      case 'x': return '#2c3e50'; // Pared
      case 'P': return '#27ae60'; // Partida
      case 'M': return '#f39c12'; // Meta
      default: return '#ecf0f1'; // Agua
    }
  }

  obtenerIconoCelda(celda: Celda, x: number, y: number): string {
    if (this.esBarco(x, y)) return '⛵';
    
    switch(celda.tipo) {
      case 'x': return '🧱';
      case 'P': return '🏁';
      case 'M': return '🎯';
      default: return '';
    }
  }

  getAccionTexto(ax: number, ay: number): string {
    if (ax === 0 && ay === 0) return 'Mantener';
    
    let texto = '';
    if (ax === -1) texto += '← ';
    if (ax === 1) texto += '→ ';
    if (ay === -1) texto += '↑ ';
    if (ay === 1) texto += '↓ ';
    
    return texto.trim() || `(${ax},${ay})`;
  }

  volverAlMenu() {
    this.router.navigate(['/partida/menu']);
  }

  pausarPartida() {
    const partida = this.partida();
    if (!partida?.id) return;

    this.partidaService.pausarPartida(partida.id).subscribe({
      next: () => {
        this.mensaje.set('Partida pausada');
        this.router.navigate(['/partida/menu']);
      },
      error: (err) => {
        console.error('Error al pausar', err);
        this.mensaje.set('Error al pausar la partida');
      }
    });
  }

  finalizarPartida() {
    const partida = this.partida();
    if (!partida?.id) return;

    if (confirm('¿Estás seguro de finalizar esta partida?')) {
      this.partidaService.finalizarPartida(partida.id).subscribe({
        next: () => {
          this.mensaje.set('Partida finalizada');
          this.router.navigate(['/partida/menu']);
        },
        error: (err) => {
          console.error('Error al finalizar', err);
          this.mensaje.set('Error al finalizar la partida');
        }
      });
    }
  }
}
