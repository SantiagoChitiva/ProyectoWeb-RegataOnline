import { Component, inject, model, signal } from '@angular/core';
import { BarcoService } from '../../shared/barco.service';
import { ModeloService } from '../../shared/modelo.service';
import { JugadorService } from '../../shared/jugador.service';
import { CeldaService } from '../../shared/celda.service';
import { Barco } from '../../model/barco';
import { Modelo } from '../../model/modelo';
import { Jugador } from '../../model/jugador';
import { Celda } from '../../model/celda';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-barco-create',
  imports: [FormsModule],
  templateUrl: './barco-create.component.html',
  styleUrl: './barco-create.component.css'
})
export class BarcoCreateComponent {
  barcoService = inject(BarcoService);
  modeloService = inject(ModeloService);
  jugadorService = inject(JugadorService);
  celdaService = inject(CeldaService);

  router = inject(Router);

  barco = model<Barco>({});
  modelos = signal<Modelo[]>([]);
  jugadores = signal<Jugador[]>([]);
  celdas = signal<Celda[]>([]);

  ngOnInit(): void {
    this.cargarModelos();
    this.cargarJugadores();
    this.cargarCeldas();
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

  cargarCeldas(): void {
    this.celdaService.findAll().subscribe({
      next: data => this.celdas.set(data),
      error: err => console.error('Error cargando celdas', err)
    });
  }

  crear() {
    console.log("Crear", this.barco());
    this.barcoService.create(this.barco()).subscribe(
      {
        next: resp => {
        console.log("Creado", resp);
        this.router.navigate(['/barco/list']);
      },
      error: err => {
        alert("Error al crear: " + err.message);
        console.log(err);
      }}
    );
  }

  cancelar() {
    this.router.navigate(['/barco/list']);
  }
}
