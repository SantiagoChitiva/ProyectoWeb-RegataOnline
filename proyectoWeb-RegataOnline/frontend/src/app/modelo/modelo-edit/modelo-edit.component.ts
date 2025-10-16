import { Component, inject, signal } from '@angular/core';
import { ModeloService } from '../../shared/modelo.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Modelo } from '../../model/modelo';

@Component({
  selector: 'app-modelo-edit',
  imports: [FormsModule], 
  templateUrl: './modelo-edit.component.html',
  styleUrl: './modelo-edit.component.css'
})
export class ModeloEditComponent {
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

  guardar() {
    this.modeloService.update(this.modelo()).subscribe({
      next: () => this.router.navigate(['/modelo/list']),
      error: err => {
        alert('Error al guardar el modelo');
        console.error(err);
      }
    });
  }

  cancelar() {
    this.router.navigate(['/modelo/list']);
  }
}