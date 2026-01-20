import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTabsModule } from '@angular/material/tabs';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AdminService } from '../services/admin.service';
import { User } from '../../../shared/models/user.model';
import { PostService, Post } from '../../posts/services/post.service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ReportReasonsDialogComponent } from '../../admin/report-reasons-dialog/report-reasons-dialog.component';

@Component({
    selector: 'app-admin-dashboard',
    standalone: true,
    imports: [CommonModule, MatTabsModule, MatTableModule, MatButtonModule, MatIconModule],
    templateUrl: './admin-dashboard.component.html',
    styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent {
    users = signal<User[]>([]);
    posts = signal<Post[]>([]);
    reportedUsers = signal<any[]>([]);
    reportedPosts = signal<any[]>([]);

    displayedUserColumns: string[] = ['username', 'email', 'role', 'actions'];
    displayedPostColumns: string[] = ['user', 'content', 'date', 'actions'];
    displayedReportUserColumns: string[] = ['username', 'reportCount', 'actions'];
    displayedReportPostColumns: string[] = ['postTitle', 'reportCount', 'actions'];

    constructor(
        private adminService: AdminService,
        private postService: PostService,
        private dialog: MatDialog
    ) { }

    ngOnInit() {
        this.loadUsers();
        this.loadPosts();
        this.loadReportedUsers();
        this.loadReportedPosts();
    }

    loadUsers() {
        this.adminService.getAllUsers().subscribe({
            next: (data) => this.users.set(data),
            error: (err) => console.error(err)
        });
    }

    loadPosts() {
        this.postService.getAllPosts().subscribe({
            next: (data) => this.posts.set(data),
            error: (err) => console.error(err)
        });
    }

    loadReportedUsers() {
        this.adminService.getReportedUsers().subscribe({
            next: (data) => this.reportedUsers.set(data),
            error: (err) => console.error(err)
        });
    }

    loadReportedPosts() {
        this.adminService.getReportedPosts().subscribe({
            next: (data) => this.reportedPosts.set(data),
            error: (err) => console.error(err)
        });
    }

    deleteUser(userId: string) {
        if (confirm('Are you sure you want to delete this user?')) {
            this.adminService.deleteUser(userId).subscribe({
                next: () => {
                    this.users.update(list => list.filter(u => u.id !== userId));
                },
                error: (err) => alert('Failed to delete user')
            });
        }
    }

    deletePost(postId: string) {
        if (confirm('Are you sure you want to delete this post?')) {
            this.adminService.deletePost(postId).subscribe({
                next: () => {
                    this.posts.update(list => list.filter(p => p.id !== postId));
                    this.loadReportedPosts(); // Reload reports as well
                },
                error: (err) => alert('Failed to delete post')
            });
        }
    }

    banUser(userId: string) {
        if (confirm('Ban this user?')) {
            this.adminService.banUser(userId).subscribe(() => {
                this.loadUsers();
                this.loadReportedUsers();
            });
        }
    }

    viewUserReportReasons(userId: string) {
        this.adminService.getReportReasonsUser(userId).subscribe(reasons => {
            this.dialog.open(ReportReasonsDialogComponent, {
                data: { reasons },
                width: '500px'
            });
        });
    }

    viewPostReportReasons(postId: string) {
        this.adminService.getReportReasonsPost(postId).subscribe(reasons => {
            this.dialog.open(ReportReasonsDialogComponent, {
                data: { reasons },
                width: '500px'
            });
        });
    }
}
