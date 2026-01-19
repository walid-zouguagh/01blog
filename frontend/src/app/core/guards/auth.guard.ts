import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    if (authService.isLoggedIn()) {
        console.log('AuthGuard: User is logged in, allowing access');
        return true;
    }

    console.log('AuthGuard: User NOT logged in, redirecting to /login');
    return router.createUrlTree(['/login']);
};
