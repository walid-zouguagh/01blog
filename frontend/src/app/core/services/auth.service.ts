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
        if (authResult.user) {
            localStorage.setItem(this.userKey, JSON.stringify(authResult.user));
            this.currentUser.set(authResult.user);
        }
    }

    private getUserFromStorage(): User | null {
        const userStr = localStorage.getItem(this.userKey);
        return userStr ? JSON.parse(userStr) : null;
    }
}
