import { Component, inject, output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Barco } from '../../model/barco';
import { BarcoService } from '../../shared/barco.service';

@Component({
  selector: 'app-barco-lista',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './barco-lista.component.html',
  styleUrl: './barco-lista.component.css'
})
export class BarcoListaComponent {
  barcos = signal<Barco[]>([]);

  barcoClicked = output<Barco>();
  barcoService = inject(BarcoService);

  ngOnInit(): void {
    this.barcoService.findAll().subscribe({
      next: data => this.barcos.set(data)
    });
  }

  barcoSelected(barco: Barco): void {
    this.barcoClicked.emit(barco);
  }
}