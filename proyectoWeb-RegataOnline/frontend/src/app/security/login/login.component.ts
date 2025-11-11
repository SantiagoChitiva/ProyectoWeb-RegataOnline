import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../shared/auth.service';
import { LoginDto } from '../../dto/login-dto';

@Component({
  selector: 'app-login',
  imports: [FormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  authService = inject(AuthService);
  router = inject(Router);

  loginDto = signal<LoginDto>({
    email: '',
    password: ''
  });

  errorMessage = signal<string>('');
  isLoading = signal<boolean>(false);

  onSubmit(): void {
    // Validación básica
    if (!this.loginDto().email || !this.loginDto().password) {
      this.errorMessage.set('Por favor ingrese email y contraseña');
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set('');

    this.authService.login(this.loginDto()).subscribe({
      next: (response) => {
        console.log('Login exitoso:', response);
        this.isLoading.set(false);
        
        // Redirigir según el rol
        if (response.role === 'ADMINISTRADOR') {
          this.router.navigate(['/jugador/list']);
        } else {
          this.router.navigate(['/partida/menu']);
        }
      },
      error: (err) => {
        console.error('Error en login:', err);
        this.isLoading.set(false);
        
        if (err.status === 401) {
          this.errorMessage.set('Email o contraseña incorrectos');
        } else if (err.status === 403) {
          this.errorMessage.set('Acceso denegado');
        } else {
          this.errorMessage.set('Error al conectar con el servidor');
        }
      }
    });
  }

  goToSignup(): void {
    this.router.navigate(['/signup']);
  }
}
