import { Component, inject, model, signal } from '@angular/core';
import { BarcoService } from '../../shared/barco.service';
import { ModeloService } from '../../shared/modelo.service';
import { JugadorService } from '../../shared/jugador.service';
import { MapaService, Mapa, Celda } from '../../shared/mapa.service';
import { Barco } from '../../model/barco';
import { Modelo } from '../../model/modelo';
import { Jugador } from '../../model/jugador';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-barco-edit',
  imports: [FormsModule, CommonModule],
  templateUrl: './barco-edit.component.html',
  styleUrl: './barco-edit.component.css'
})
export class BarcoEditComponent {
  barcoService = inject(BarcoService);
  modeloService = inject(ModeloService);
  jugadorService = inject(JugadorService);
  mapaService = inject(MapaService);

  route = inject(ActivatedRoute);
  router = inject(Router);

  barco = model<Barco>({});
  modelos = signal<Modelo[]>([]);
  jugadores = signal<Jugador[]>([]);
  
  // Signals para mapa visual
  mapas = signal<Mapa[]>([]);
  mapaSeleccionado = signal<Mapa | null>(null);
  matrizCeldas = signal<(Celda | null)[][]>([]);
  celdaSeleccionada = signal<Celda | null>(null);

  ngOnInit(): void {
    this.cargarModelos();
    this.cargarJugadores();
    this.cargarMapas();
    
    this.route.params.pipe(
      switchMap(params => this.barcoService.findById(+params['id']))
    ).subscribe(resp => {
      this.barco.set(resp);
      // Si el barco tiene una celda asignada, cargar su mapa
      if (resp.celdaId) {
        this.cargarMapaDeCelda(resp.celdaId);
      }
    });
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

  cargarMapaDeCelda(celdaId: number): void {
    // Buscar el mapa que contiene esta celda
    this.mapas().forEach(mapa => {
      if (!mapa.celdas) return;
      const celdaEncontrada = mapa.celdas.find(c => c.id === celdaId);
      if (celdaEncontrada) {
        this.mapaSeleccionado.set(mapa);
        this.construirMatriz(mapa);
        this.celdaSeleccionada.set(celdaEncontrada);
      }
    });
  }

  onMapaSeleccionado(event: Event): void {
    const target = event.target as HTMLSelectElement;
    const mapaId = +target.value;
    
    if (!mapaId) {
      this.mapaSeleccionado.set(null);
      this.matrizCeldas.set([]);
      this.celdaSeleccionada.set(null);
      return;
    }

    this.mapaService.buscarMapa(mapaId).subscribe({
      next: mapa => {
        this.mapaSeleccionado.set(mapa);
        this.construirMatriz(mapa);
      },
      error: err => console.error('Error cargando mapa', err)
    });
  }

  construirMatriz(mapa: Mapa): void {
    const matriz: (Celda | null)[][] = [];
    
    for (let i = 0; i < mapa.filas; i++) {
      matriz[i] = [];
      for (let j = 0; j < mapa.columnas; j++) {
        const celda = mapa.celdas?.find(c => c.posicionX === j && c.posicionY === i);
        matriz[i][j] = celda || null;
      }
    }
    
    this.matrizCeldas.set(matriz);
  }

  seleccionarCelda(celda: Celda | null): void {
    if (!celda) return;
    
    // No permitir seleccionar paredes
    if (celda.tipo === 'x') {
      alert('No puedes posicionar el barco en una pared');
      return;
    }
    
    this.celdaSeleccionada.set(celda);
    
    // Actualizar el barco con la nueva celda
    const barcoActual = this.barco();
    barcoActual.celdaId = celda.id;
    barcoActual.posicionX = celda.posicionX;
    barcoActual.posicionY = celda.posicionY;
    this.barco.set({ ...barcoActual });
  }

  obtenerColorCelda(celda: Celda | null): string {
    if (!celda) return '#ecf0f1';
    
    switch(celda.tipo) {
      case 'x': return '#2c3e50'; // Pared
      case 'P': return '#27ae60'; // Partida
      case 'M': return '#e74c3c'; // Meta
      default: return '#3498db';  // Agua
    }
  }

  obtenerIconoCelda(celda: Celda | null): string {
    if (!celda) return '';
    
    switch(celda.tipo) {
      case 'x': return '🧱';
      case 'P': return '🏁';
      case 'M': return '🎯';
      default: return '🌊';
    }
  }

  esCeldaSeleccionada(celda: Celda | null): boolean {
    return celda !== null && 
           this.celdaSeleccionada() !== null && 
           celda.id === this.celdaSeleccionada()?.id;
  }

  esCeldaNoNavegable(celda: Celda | null): boolean {
    return celda !== null && celda.tipo === 'x';
  }

  guardar() {
    console.log("Guardar", this.barco());
    this.barcoService.update(this.barco()).subscribe({
      next: resp => {
        console.log("Guardado", resp);
        this.router.navigate(['/barco/list']);
      },
      error: err => {
        alert("Error al guardar: " + err.message);
        console.log(err);
      }
    });
  }

  cancelar() {
    this.router.navigate(['/barco/list']);
  }
}
