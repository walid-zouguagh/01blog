import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { map } from 'rxjs/operators';

export const adminGuard: CanActivateFn = (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    // We need to check if user has ROLE_ADMIN. 
    // Access currentUser signal from AuthService
    const user = authService.currentUser();

    if (user && user.role === 'ADMIN') {
        return true;
    }

    // If not admin, redirect to home or login
    return router.createUrlTree(['/']);
};
