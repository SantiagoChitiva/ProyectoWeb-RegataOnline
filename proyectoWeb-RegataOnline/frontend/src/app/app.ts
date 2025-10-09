import { Component, signal } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { BarcoViewComponent } from './barco/barco-view/barco-view.component';
import { BarcoListaComponent } from './barco/barco-lista/barco-lista.component';
import { Barco } from './model/barco';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, BarcoViewComponent, BarcoListaComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
}
