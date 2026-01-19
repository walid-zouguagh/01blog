import { Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from './api.service';
import { HttpClient } from '@angular/common/http';
import { map, Observable, tap } from 'rxjs'; // Fix import
import { LoginDto } from '../../features/auth/models/login.dto';
import { RegisterDto } from '../../features/auth/models/register.dto';
import { AuthResponse } from '../../features/auth/models/auth-response.dto';

import { User } from '../../shared/models/user.model';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private userKey = '01blog_user';
    private tokenKey = 'token';

    // Signals for reactive state
    currentUser = signal<User | null>(this.getUserFromStorage());

    constructor(private api: ApiService, private router: Router, private http: HttpClient) { }

    login(credentials: LoginDto): Observable<AuthResponse> {
        return this.http.post<AuthResponse>('http://localhost:8080/auth/login', credentials).pipe(
            tap(response => this.setSession(response))
        );
    }

    register(data: RegisterDto): Observable<AuthResponse> {
        return this.http.post<AuthResponse>('http://localhost:8080/auth/register', data).pipe(
            tap(response => this.setSession(response))
        );
    }

    logout() {
        localStorage.removeItem(this.tokenKey);
        localStorage.removeItem(this.userKey);
        this.currentUser.set(null);
        this.router.navigate(['/login']);
    }

    isLoggedIn(): boolean {
        return !!localStorage.getItem(this.tokenKey);
    }

    private setSession(authResult: AuthResponse) {
        localStorage.setItem(this.tokenKey, authResult.token);
        // Since AuthResponse might not have full user details, we might decode token or fetch profile
        // For now assuming we parse token or get it separately.
        // If backend returns user in response:
        // const user = authResult.user;
        // localStorage.setItem(this.userKey, JSON.stringify(user));
        // this.currentUser.set(user);
        // If not, we might need a 'me' endpoint or decode JWT.
    }

    private getUserFromStorage(): User | null {
        const userStr = localStorage.getItem(this.userKey);
        return userStr ? JSON.parse(userStr) : null;
    }
}
