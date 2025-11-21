import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../shared/auth.service';

export const jugadorGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isAuthenticated()) {
    router.navigateByUrl('/login');
    return false;
  }

  if (authService.role() === 'JUGADOR') {
    return true;
  } else {
    // Si no es jugador, redirigir al área de administración
    router.navigateByUrl('/jugador/list');
    return false;
  }
};
