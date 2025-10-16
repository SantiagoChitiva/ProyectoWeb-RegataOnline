import { Component, inject, signal } from '@angular/core';
import { ModeloService } from '../../shared/modelo.service';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Modelo } from '../../model/modelo';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-modelo-view',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './modelo-view.component.html',
  styleUrl: './modelo-view.component.css'
})
export class ModeloViewComponent {
  modeloService = inject(ModeloService);
  route = inject(ActivatedRoute);
  router = inject(Router);

  modelo = signal<Modelo>({ nombreModelo: '', color: '#3498db' });

  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];
    this.modeloService.findById(id).subscribe({
      next: data => this.modelo.set(data),
      error: err => console.error(err)
    });
  }

  volver() {
    this.router.navigate(['/modelo/list']);
  }
}