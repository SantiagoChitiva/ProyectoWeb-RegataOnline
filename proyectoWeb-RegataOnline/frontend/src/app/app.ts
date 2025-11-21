import { Component, computed, inject, signal } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { BarcoViewComponent } from './barco/barco-view/barco-view.component';
import { BarcoListaComponent } from './barco/barco-lista/barco-lista.component';
import { Barco } from './model/barco';
import { AuthService } from './shared/auth.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, BarcoViewComponent, BarcoListaComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  private authService = inject(AuthService);
  private router = inject(Router);

  // Usamos el signal reactivo del servicio
  isAuthenticated = this.authService.isAuthenticatedSignal;

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
