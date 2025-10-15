import { Component, inject, model, signal } from '@angular/core';
import { ModeloService } from '../../shared/modelo.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-modelo-create',
  imports: [FormsModule],
  templateUrl: './modelo-create.component.html',
  styleUrl: './modelo-create.component.css'
})
export class ModeloCreateComponent {
  modeloService = inject(ModeloService);
  router = inject(Router);

  modelo = model<any>({});

  crear() {
    this.modeloService.create(this.modelo()).subscribe({
      next: () => this.router.navigate(['/modelo/list']),
      error: err => {
        alert('Error al crear el modelo');
        console.error(err);
      }
    });
  }

  cancelar() {
    this.router.navigate(['/modelo/list']);
  }
}