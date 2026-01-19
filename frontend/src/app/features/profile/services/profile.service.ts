import { Injectable } from '@angular/core';
import { ApiService } from '../../../core/services/api.service';
import { Observable } from 'rxjs';
import { User } from '../../../shared/models/user.model';
import { HttpParams } from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class ProfileService {

    constructor(private api: ApiService) { }

    getProfile(id: string): Observable<User> {
        return this.api.get<User>(`profile/${id}`);
    }

    getCurrentUser(): Observable<User> {
        return this.api.get<User>('forme');
    }

    followUser(userId: string): Observable<any> {
        const params = new HttpParams().set('userId', userId);
        return this.api.post<any>('follow', {}, params);
    }

    searchUsers(name: string): Observable<User[]> {
        const params = new HttpParams().set('name', name);
        return this.api.get<User[]>('search', params);
    }
}
