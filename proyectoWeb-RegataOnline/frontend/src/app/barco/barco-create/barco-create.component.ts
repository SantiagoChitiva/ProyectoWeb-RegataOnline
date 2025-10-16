import { Component, inject, model, signal } from '@angular/core';
import { BarcoService } from '../../shared/barco.service';
import { ModeloService } from '../../shared/modelo.service';
import { JugadorService } from '../../shared/jugador.service';
import { MapaService, Mapa, Celda as CeldaMapa } from '../../shared/mapa.service';
import { Barco } from '../../model/barco';
import { Modelo } from '../../model/modelo';
import { Jugador } from '../../model/jugador';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-barco-create',
  imports: [FormsModule, CommonModule],
  templateUrl: './barco-create.component.html',
  styleUrl: './barco-create.component.css'
})
export class BarcoCreateComponent {
  barcoService = inject(BarcoService);
  modeloService = inject(ModeloService);
  jugadorService = inject(JugadorService);
  mapaService = inject(MapaService);
  router = inject(Router);

  barco = model<Barco>({});
  modelos = signal<Modelo[]>([]);
  jugadores = signal<Jugador[]>([]);
  mapas = signal<Mapa[]>([]);
  
  // Mapa seleccionado
  mapaSeleccionadoId = signal<number | null>(null);
  mapaSeleccionado = signal<Mapa | null>(null);
  matrizCeldas = signal<CeldaMapa[][]>([]);
  celdaSeleccionada = signal<CeldaMapa | null>(null);

  ngOnInit(): void {
    this.cargarModelos();
    this.cargarJugadores();
    this.cargarMapas();
  }

  cargarModelos(): void {
    this.modeloService.findAll().subscribe({
      next: data => this.modelos.set(data),
      error: err => console.error('Error cargando modelos', err)
    });
  }

  cargarJugadores(): void {
    this.jugadorService.findAll().subscribe({
      next: data => this.jugadores.set(data),
      error: err => console.error('Error cargando jugadores', err)
    });
  }

  cargarMapas(): void {
    console.log('Cargando mapas...');
    this.mapaService.listarMapas().subscribe({
      next: data => {
        console.log('Mapas recibidos:', data);
        this.mapas.set(data);
      },
      error: err => console.error('Error cargando mapas', err)
    });
  }

  onMapaSeleccionado(event: Event): void {
    const select = event.target as HTMLSelectElement;
    const mapaId = Number(select.value);
    
    if (!mapaId) {
      this.mapaSeleccionado.set(null);
      this.matrizCeldas.set([]);
      this.celdaSeleccionada.set(null);
      return;
    }

    this.mapaService.buscarMapa(mapaId).subscribe({
      next: (mapa) => {
        this.mapaSeleccionado.set(mapa);
        this.construirMatriz(mapa);
      },
      error: err => console.error('Error cargando mapa', err)
    });
  }

  construirMatriz(mapa: Mapa): void {
    if (!mapa.celdas) return;

    const matriz: CeldaMapa[][] = [];
    
    // Crear matriz vacía con agua
    // i = fila (posicionY), j = columna (posicionX)
    for (let i = 0; i < mapa.filas; i++) {
      const fila: CeldaMapa[] = [];
      for (let j = 0; j < mapa.columnas; j++) {
        fila.push({
          id: undefined,
          posicionX: j,  // j es la columna = X
          posicionY: i,  // i es la fila = Y
          tipo: ''
        });
      }
      matriz.push(fila);
    }

    // Llenar con las celdas reales del mapa
    // Backend: posicionX = columna, posicionY = fila
    mapa.celdas.forEach(celda => {
      // Buscar en matriz[fila][columna] = matriz[posicionY][posicionX]
      if (celda.posicionY < mapa.filas && celda.posicionX < mapa.columnas) {
        matriz[celda.posicionY][celda.posicionX] = celda;
      }
    });

    this.matrizCeldas.set(matriz);
  }

  seleccionarCelda(celda: CeldaMapa): void {
    // Verificar que la celda sea navegable
    if (celda.tipo === 'x') {
      alert('No puedes posicionar un barco en una pared');
      return;
    }

    this.celdaSeleccionada.set(celda);
    
    // Actualizar el barco con los datos de la celda
    const barcoActual = this.barco();
    barcoActual.celdaId = celda.id;
    barcoActual.posicionX = celda.posicionX;
    barcoActual.posicionY = celda.posicionY;
    this.barco.set({ ...barcoActual });
  }

  obtenerColorCelda(celda: CeldaMapa): string {
    const estaSeleccionada = this.celdaSeleccionada()?.posicionX === celda.posicionX && 
                             this.celdaSeleccionada()?.posicionY === celda.posicionY;
    
    if (estaSeleccionada) return '#f39c12'; // Naranja para seleccionada
    
    switch(celda.tipo) {
      case 'x': return '#2c3e50';
      case 'P': return '#27ae60';
      case 'M': return '#e74c3c';
      default: return '#3498db';
    }
  }

  obtenerIconoCelda(celda: CeldaMapa): string {
    const estaSeleccionada = this.celdaSeleccionada()?.posicionX === celda.posicionX && 
                             this.celdaSeleccionada()?.posicionY === celda.posicionY;
    
    if (estaSeleccionada) return '⛵';
    
    switch(celda.tipo) {
      case 'x': return '🧱';
      case 'P': return '🏁';
      case 'M': return '🎯';
      default: return '🌊';
    }
  }

  crear() {
    if (!this.barco().celdaId) {
      alert('Debes seleccionar una celda en el mapa');
      return;
    }

    console.log("Crear", this.barco());
    this.barcoService.create(this.barco()).subscribe({
      next: resp => {
        console.log("Creado", resp);
        this.router.navigate(['/barco/list']);
      },
      error: err => {
        alert("Error al crear: " + err.message);
        console.log(err);
      }
    });
  }

  cancelar() {
    this.router.navigate(['/barco/list']);
  }
}
