import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class ApiService {
    private baseUrl = environment.apiUrl;

    constructor(private http: HttpClient) { }

    private get headers(): HttpHeaders {
        const token = localStorage.getItem('token');
        let headers = new HttpHeaders({
            'Accept': 'application/json'
        });
        if (token) {
            headers = headers.set('Authorization', `Bearer ${token}`);
        }
        return headers;
    }

    get<T>(path: string, params: HttpParams = new HttpParams()): Observable<T> {
        return this.http.get<T>(`${this.baseUrl}/${path}`, { headers: this.headers, params });
    }

    post<T>(path: string, body: Object = {}, params: HttpParams = new HttpParams()): Observable<T> {
        return this.http.post<T>(`${this.baseUrl}/${path}`, body, { headers: this.headers, params });
    }

    // Generic Put
    put<T>(path: string, body: Object = {}, params: HttpParams = new HttpParams()): Observable<T> {
        return this.http.put<T>(`${this.baseUrl}/${path}`, body, { headers: this.headers, params });
    }

    // Generic Delete
    delete<T>(path: string, params: HttpParams = new HttpParams()): Observable<T> {
        return this.http.delete<T>(`${this.baseUrl}/${path}`, { headers: this.headers, params });
    }

    // For file uploads (handling FormData)
    postMedia<T>(path: string, body: FormData): Observable<T> {
        // Content-Type is handled automatically by browser for FormData
        const token = localStorage.getItem('token');
        let headers = new HttpHeaders();
        if (token) {
            headers = headers.set('Authorization', `Bearer ${token}`);
        }
        return this.http.post<T>(`${this.baseUrl}/${path}`, body, { headers });
    }
}
