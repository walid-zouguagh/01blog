import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatBadgeModule } from '@angular/material/badge';
import { AuthService } from '../../../core/services/auth.service';
import { NotificationService, NotificationDto } from '../../../features/notifications/services/notification.service';

@Component({
    selector: 'app-nav-bar',
    standalone: true,
    imports: [CommonModule, RouterLink, RouterLinkActive, MatToolbarModule, MatButtonModule, MatIconModule, MatMenuModule, MatBadgeModule],
    templateUrl: './nav-bar.component.html',
    styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent {
    notifications = signal<NotificationDto[]>([]);
    unreadCount = signal(0);

    constructor(public auth: AuthService, private notifService: NotificationService) { }

    ngOnInit() {
        if (this.auth.currentUser()) {
            this.loadNotifications();
        }
    }

    loadNotifications() {
        this.notifService.getNotifications().subscribe({
            next: (data: NotificationDto[]) => {
                this.notifications.set(data);
                this.unreadCount.set(data.filter((n: NotificationDto) => !n.isRead).length);
            }
        });
    }

    markAsRead(id: string) {
        this.notifService.readNotification(id).subscribe(() => {
            this.notifications.update(list => list.map(n => n.id === id ? { ...n, isRead: true } : n));
            this.unreadCount.update(c => Math.max(0, c - 1));
        });
    }

    markAllAsRead() {
        this.notifService.readAll().subscribe(() => {
            this.notifications.update(list => list.map(n => ({ ...n, isRead: true })));
            this.unreadCount.set(0);
        });
    }

    logout() {
        this.auth.logout();
    }
}
