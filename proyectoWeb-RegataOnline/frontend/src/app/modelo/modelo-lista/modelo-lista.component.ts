import { Component, inject, signal } from '@angular/core';
import { ModeloService } from '../../shared/modelo.service';
import { Router } from '@angular/router';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-modelo-lista',
    standalone: true,
    imports: [CommonModule, RouterLink],
    templateUrl: './modelo-lista.component.html',
    styleUrl: './modelo-lista.component.css'
  })
export class ModeloListaComponent {
  modelos = signal<any[]>([]);
  modeloService = inject(ModeloService);

  ngOnInit(): void {
    this.cargarModelos();
  }

  cargarModelos() {
    this.modeloService.findAll().subscribe({
      next: data => this.modelos.set(data),
      error: err => console.error(err)
    });
  }

  eliminar(id: number | undefined, nombre: string | undefined): void {
    if (!id) return;
    
    if (confirm(`¿Estás seguro de eliminar el modelo "${nombre}"?`)) {
      this.modeloService.delete(id).subscribe({
        next: () => {
          console.log('Modelo eliminado');
          this.cargarModelos();
        },
        error: err => {
          alert('Error al eliminar el modelo');
          console.error(err);
        }
      });
    }
  }
}