import { Injectable } from '@angular/core';
import { ApiService } from '../../../core/services/api.service';
import { Observable } from 'rxjs';

export interface NotificationDto {
    id: string; // UUID
    userId: string;
    description: string;
    read: boolean;
    createdAt?: string;
    // userFrom? post?
}

@Injectable({
    providedIn: 'root'
})
export class NotificationService {

    constructor(private api: ApiService) { }

    getNotifications(): Observable<NotificationDto[]> {
        return this.api.get<NotificationDto[]>('notifications');
    }

    getCount(): Observable<number> {
        return this.api.get<number>('countNotification');
    }

    readNotification(notifId: string): Observable<void> {
        return this.api.put(`read-notification/${notifId}`);
    }

    readAll(): Observable<void> {
        return this.api.put('read-all-notification');
    }
}
