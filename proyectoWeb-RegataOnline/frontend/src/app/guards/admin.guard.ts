import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../shared/auth.service';

export const adminGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isAuthenticated()) {
    router.navigateByUrl('/login');
    return false;
  }

  if (authService.role() === 'ADMINISTRADOR') {
    return true;
  } else {
    // Si no es admin, redirigir al menú de partida (área de jugador)
    router.navigateByUrl('/partida/menu');
    return false;
  }
};
