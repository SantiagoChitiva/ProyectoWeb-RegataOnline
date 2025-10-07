import { Component, inject, input, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Jugador } from '../../model/jugador';
import { JugadorService } from '../../shared/jugador.service';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap } from 'rxjs';

@Component({
  selector: 'app-jugador-view',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './jugador-view.component.html',
  styleUrl: './jugador-view.component.css'
})
export class JugadorViewComponent {
  jugador = signal<Jugador>({});
  jugadorService = inject(JugadorService);
  route = inject(ActivatedRoute);
  router = inject(Router);

  ngOnInit(): void {
    this.route.params.pipe(
      switchMap(params => this.jugadorService.findById(+params['id']))
    ).subscribe({
      next: data => this.jugador.set(data),
      error: err => console.error('Error cargando jugador', err)
    });
  }

  volver(): void {
    this.router.navigate(['/jugador/list']);
  }
}
