import { Component, inject, model } from '@angular/core';
import { JugadorService } from '../../shared/jugador.service';
import { Jugador } from '../../model/jugador';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-jugador-create',
  imports: [FormsModule],
  templateUrl: './jugador-create.component.html',
  styleUrl: './jugador-create.component.css'
})
export class JugadorCreateComponent {
  jugadorService = inject(JugadorService);
  router = inject(Router);

  jugador = model<Jugador>({});

  crear() {
    console.log("Crear", this.jugador());
    this.jugadorService.create(this.jugador()).subscribe(
      {
        next: resp => {
        console.log("Creado", resp);
        this.router.navigate(['/jugador/list']);
      },
      error: err => {
        alert("Error al crear: " + err.message);
        console.log(err);
      }}
    );
  }

  cancelar() {
    this.router.navigate(['/jugador/list']);
  }
}
