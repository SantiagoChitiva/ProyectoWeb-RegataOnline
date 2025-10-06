import { Component, input, signal, WritableSignal } from '@angular/core';
import { Barco } from '../../model/barco';

@Component({
  selector: 'app-barco-view',
  imports: [],
  templateUrl: './barco-view.component.html',
  styleUrl: './barco-view.component.css'
})
export class BarcoViewComponent {
  barco = input<Barco>({});
}
