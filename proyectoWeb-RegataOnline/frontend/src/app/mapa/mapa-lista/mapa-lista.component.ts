import { Component, inject, signal, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MapaService, Mapa } from '../../shared/mapa.service';

@Component({
  selector: 'app-mapa-lista',
  imports: [CommonModule, RouterLink],
  templateUrl: './mapa-lista.component.html',
  styleUrl: './mapa-lista.component.css'
})
export class MapaListaComponent implements OnInit {
  mapaService = inject(MapaService);
  router = inject(Router);

  mapas = signal<Mapa[]>([]);
  cargando = signal(true);

  ngOnInit(): void {
    this.cargarMapas();
  }

  cargarMapas() {
    this.cargando.set(true);
    this.mapaService.listarMapas().subscribe({
      next: (mapas) => {
        this.mapas.set(mapas);
        this.cargando.set(false);
      },
      error: (error) => {
        console.error('Error cargando mapas', error);
        this.cargando.set(false);
      }
    });
  }

  verDetalle(id: number | undefined) {
    if (id) {
      this.router.navigate(['/mapa/view', id]);
    }
  }

  eliminarMapa(id: number | undefined, event: Event) {
    event.stopPropagation();
    
    if (!id) return;
    
    if (confirm('¿Estás seguro de eliminar este mapa?')) {
      this.mapaService.borrarMapa(id).subscribe({
        next: () => {
          this.cargarMapas();
        },
        error: (error) => {
          console.error('Error eliminando mapa', error);
          const mensaje = error.error || 'Error al eliminar el mapa';
          alert(mensaje);
        }
      });
    }
  }

  crearNuevoMapa() {
    this.router.navigate(['/mapa/create']);
  }
}
