import { Component, inject, signal } from '@angular/core';
import { ModeloService } from '../../shared/modelo.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-modelo-view',
  templateUrl: './modelo-view.component.html',
  styleUrl: './modelo-view.component.css'
})
export class ModeloViewComponent {
  modeloService = inject(ModeloService);
  route = inject(ActivatedRoute);
  router = inject(Router);

  modelo = signal<any>({});

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