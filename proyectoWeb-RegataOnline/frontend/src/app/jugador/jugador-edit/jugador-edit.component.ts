import { Component, inject, model } from '@angular/core';
import { JugadorService } from '../../shared/jugador.service';
import { Jugador } from '../../model/jugador';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap } from 'rxjs';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-jugador-edit',
  imports: [FormsModule],
  templateUrl: './jugador-edit.component.html',
  styleUrl: './jugador-edit.component.css'
})
export class JugadorEditComponent {
  jugadorService = inject(JugadorService);
  route = inject(ActivatedRoute);
  router = inject(Router);

  jugador = model<Jugador>({});

  ngOnInit(): void {
    this.route.params.pipe(
      switchMap(params => this.jugadorService.findById(+params['id']))
    ).subscribe(resp => this.jugador.set(resp));
  }

  guardar() {
    console.log("Guardar", this.jugador());
    this.jugadorService.update(this.jugador()).subscribe(
      {
        next: resp => {
        console.log("Guardado", resp);
        this.router.navigate(['/jugador/list']);
      },
      error: err => {
        alert("Error al guardar");
        console.log(err);
      }}
    );
  }
}
