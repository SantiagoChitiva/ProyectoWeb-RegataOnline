import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../shared/auth.service';
import { SignupDto } from '../../dto/signup-dto';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  signupDto = signal<SignupDto>({
    nombre: '',
    email: '',
    password: '',
    confirmPassword: '',
    role: 'JUGADOR'
  });

  errorMessage = signal<string>('');
  isLoading = signal<boolean>(false);

  onSubmit() {
    this.errorMessage.set('');

    // Validaciones
    if (!this.signupDto().nombre || !this.signupDto().email || !this.signupDto().password) {
      this.errorMessage.set('Todos los campos son obligatorios');
      return;
    }

    if (this.signupDto().password !== this.signupDto().confirmPassword) {
      this.errorMessage.set('Las contraseñas no coinciden');
      return;
    }

    if (this.signupDto().password.length < 6) {
      this.errorMessage.set('La contraseña debe tener al menos 6 caracteres');
      return;
    }

    this.isLoading.set(true);

    // Crear objeto sin confirmPassword
    const { confirmPassword, ...signupData } = this.signupDto();

    this.authService.signup(signupData).subscribe({
      next: (response) => {
        this.isLoading.set(false);
        // Redirigir según el rol
        if (response.role === 'ADMINISTRADOR') {
          this.router.navigate(['/jugador/list']);
        } else {
          this.router.navigate(['/partida/menu']);
        }
      },
      error: (err) => {
        this.isLoading.set(false);
        if (err.status === 400) {
          this.errorMessage.set('El email ya está registrado');
        } else if (err.status === 403) {
          this.errorMessage.set('Acceso denegado');
        } else {
          this.errorMessage.set('Error al crear la cuenta. Intenta nuevamente.');
        }
      }
    });
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }
}
