import { Component, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { switchMap } from 'rxjs';
import { MapaService, Mapa, Celda } from '../../shared/mapa.service';

@Component({
  selector: 'app-mapa-view',
  imports: [CommonModule, RouterLink],
  templateUrl: './mapa-view.component.html',
  styleUrl: './mapa-view.component.css'
})
export class MapaViewComponent implements OnInit {
  mapaService = inject(MapaService);
  route = inject(ActivatedRoute);
  router = inject(Router);

  mapa = signal<Mapa | null>(null);
  matrizCeldas = signal<Celda[][]>([]);
  cargando = signal(true);

  ngOnInit(): void {
    this.route.params.pipe(
      switchMap(params => this.mapaService.buscarMapa(+params['id']))
    ).subscribe({
      next: (mapa) => {
        this.mapa.set(mapa);
        this.construirMatriz(mapa);
        this.cargando.set(false);
      },
      error: (error) => {
        console.error('Error cargando mapa', error);
        this.cargando.set(false);
      }
    });
  }

  construirMatriz(mapa: Mapa) {
    if (!mapa.celdas) return;

    const matriz: Celda[][] = [];
    
    // Crear matriz vacía
    for (let i = 0; i < mapa.filas; i++) {
      const fila: Celda[] = [];
      for (let j = 0; j < mapa.columnas; j++) {
        fila.push({
          posicionX: i,
          posicionY: j,
          tipo: ''
        });
      }
      matriz.push(fila);
    }

    // Llenar con las celdas del mapa
    mapa.celdas.forEach(celda => {
      if (celda.posicionX < mapa.filas && celda.posicionY < mapa.columnas) {
        matriz[celda.posicionX][celda.posicionY] = celda;
      }
    });

    this.matrizCeldas.set(matriz);
  }

  obtenerColorCelda(tipo: string): string {
    switch(tipo) {
      case 'x': return '#2c3e50';
      case 'P': return '#27ae60';
      case 'M': return '#e74c3c';
      default: return '#3498db';
    }
  }

  obtenerIconoCelda(tipo: string): string {
    switch(tipo) {
      case 'x': return '🧱';
      case 'P': return '🏁';
      case 'M': return '🎯';
      default: return '🌊';
    }
  }

  contarCeldasPorTipo(tipo: string): number {
    if (!this.mapa()?.celdas) return 0;
    return this.mapa()!.celdas!.filter(c => c.tipo === tipo).length;
  }

  volver() {
    this.router.navigate(['/mapa/list']);
  }

  eliminar() {
    const id = this.mapa()?.id;
    if (!id) return;

    if (confirm('¿Estás seguro de eliminar este mapa?')) {
      this.mapaService.borrarMapa(id).subscribe({
        next: () => {
          this.router.navigate(['/mapa/list']);
        },
        error: (error) => {
          console.error('Error eliminando mapa', error);
          const mensaje = error.error || 'Error al eliminar el mapa';
          alert(mensaje);
        }
      });
    }
  }
}
