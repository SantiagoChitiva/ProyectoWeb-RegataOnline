import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MapaService, Celda, CrearMapaRequest } from '../../shared/mapa.service';

@Component({
  selector: 'app-mapa-create',
  imports: [FormsModule, CommonModule],
  templateUrl: './mapa-create.component.html',
  styleUrl: './mapa-create.component.css'
})
export class MapaCreateComponent {
  mapaService = inject(MapaService);
  router = inject(Router);

  // Dimensiones del mapa
  filas = signal(10);
  columnas = signal(10);
  
  // Mapa generado
  mapaGenerado = signal(false);
  celdas = signal<Celda[][]>([]);
  
  // Tipo de celda seleccionado
  tipoSeleccionado = signal('x'); // 'x' = pared, 'P' = partida, 'M' = meta, '' = agua
  
  generarMapa() {
    const filasVal = this.filas();
    const columnasVal = this.columnas();
    
    // Validaciones
    if (filasVal < 5 || filasVal > 20) {
      alert('El número de filas debe estar entre 5 y 20');
      return;
    }
    
    if (columnasVal < 5 || columnasVal > 20) {
      alert('El número de columnas debe estar entre 5 y 20');
      return;
    }
    
    // Generar matriz de celdas
    const nuevasCeldas: Celda[][] = [];
    for (let i = 0; i < filasVal; i++) {
      const fila: Celda[] = [];
      for (let j = 0; j < columnasVal; j++) {
        fila.push({
          posicionX: i,
          posicionY: j,
          tipo: ''
        });
      }
      nuevasCeldas.push(fila);
    }
    
    this.celdas.set(nuevasCeldas);
    this.mapaGenerado.set(true);
  }
  
  toggleCelda(celda: Celda) {
    celda.tipo = this.tipoSeleccionado();
  }
  
  obtenerColorCelda(tipo: string): string {
    switch(tipo) {
      case 'x': return '#2c3e50'; // Pared - gris oscuro
      case 'P': return '#27ae60'; // Partida - verde
      case 'M': return '#e74c3c'; // Meta - rojo
      default: return '#3498db'; // Agua - azul
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
  
  guardarMapa() {
    const celdasSeleccionadas: Celda[] = [];
    
    // Recopilar solo las celdas que no son agua
    this.celdas().forEach(fila => {
      fila.forEach(celda => {
        if (celda.tipo !== '') {
          celdasSeleccionadas.push({
            posicionX: celda.posicionX,
            posicionY: celda.posicionY,
            tipo: celda.tipo
          });
        }
      });
    });
    
    const request: CrearMapaRequest = {
      filas: this.filas(),
      columnas: this.columnas(),
      celdasSeleccionadas: celdasSeleccionadas
    };
    
    this.mapaService.crearMapa(request).subscribe({
      next: (mapa) => {
        console.log('Mapa creado exitosamente', mapa);
        alert('Mapa creado exitosamente');
        this.router.navigate(['/mapa/list']);
      },
      error: (error) => {
        console.error('Error al crear mapa', error);
        alert('Error al crear el mapa');
      }
    });
  }
  
  limpiarMapa() {
    this.celdas().forEach(fila => {
      fila.forEach(celda => {
        celda.tipo = '';
      });
    });
  }
  
  cancelar() {
    this.router.navigate(['/']);
  }
}
