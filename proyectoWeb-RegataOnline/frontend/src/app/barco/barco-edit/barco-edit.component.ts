import { Component, inject, model, signal } from '@angular/core';
import { BarcoService } from '../../shared/barco.service';
import { Barco } from '../../model/barco';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap } from 'rxjs';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-barco-edit',
  imports: [FormsModule],
  templateUrl: './barco-edit.component.html',
  styleUrl: './barco-edit.component.css'
})
export class BarcoEditComponent {
  barcoService = inject(BarcoService);

  route = inject(ActivatedRoute);

  router = inject(Router);

  barco = model<Barco>({});

  ngOnInit(): void {
    this.route.params.pipe(
      switchMap(params => this.barcoService.findById(+params['id']))
    ).subscribe(resp => this.barco.set(resp));
  }

  guardar() {
    console.log("Guardar", this.barco());
    this.barcoService.update(this.barco()).subscribe(
      {
        next: resp => {
        console.log("Guardado", resp);
        this.router.navigate(['/barco/list']);
      },
      error: err => {
        alert("Error al guardar: ");
        console.log(err);
      }}
    );
  }

}
