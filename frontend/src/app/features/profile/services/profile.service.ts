import { Injectable } from '@angular/core';
import { ApiService } from '../../../core/services/api.service';
import { Observable } from 'rxjs';
import { User } from '../../../shared/models/user.model';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class ProfileService {

    constructor(private api: ApiService, private http: HttpClient) { }

    getProfile(id: string): Observable<User> {
        const token = localStorage.getItem('token');
        const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`
        });
        return this.http.get<User>(`http://localhost:8080/auth/profile/${id}`, { headers });
    }

    getCurrentUser(): Observable<User> {
        const token = localStorage.getItem('token');
        const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`
        });
        return this.http.get<User>('http://localhost:8080/auth/forme', { headers });
    }

    followUser(userId: string): Observable<any> {
        const params = new HttpParams().set('userId', userId);
        return this.api.post<any>('follow', {}, params);
    }

    searchUsers(name: string): Observable<User[]> {
        const token = localStorage.getItem('token');
        const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`
        });
        const params = new HttpParams().set('name', name);
        return this.http.get<User[]>('http://localhost:8080/auth/search', { headers, params });
    }
}
