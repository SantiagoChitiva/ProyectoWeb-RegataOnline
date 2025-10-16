import { Component, inject, output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Barco } from '../../model/barco';
import { BarcoService } from '../../shared/barco.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-barco-lista',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './barco-lista.component.html',
  styleUrl: './barco-lista.component.css'
})
export class BarcoListaComponent {
  barcos = signal<Barco[]>([]);

  barcoClicked = output<Barco>();
  barcoService = inject(BarcoService);

  ngOnInit(): void {
    this.cargarBarcos();
  }

  cargarBarcos(): void {
    this.barcoService.findAll().subscribe({
      next: data => this.barcos.set(data)
    });
  }

  barcoSelected(barco: Barco): void {
    this.barcoClicked.emit(barco);
  }

  eliminar(id: number | undefined, nombre: string | undefined): void {
    if (!id) return;
    
    if (confirm(`¿Estás seguro de eliminar el barco "${nombre}"?`)) {
      this.barcoService.delete(id).subscribe({
        next: () => {
          console.log('Barco eliminado');
          this.cargarBarcos();
        },
        error: err => {
          alert('Error al eliminar el barco');
          console.error(err);
        }
      });
    }
  }
}