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

    postsOffset = signal(0);
    hasMorePosts = signal(true);
    usersOffset = signal(0);
    hasMoreUsers = signal(true);

    // ...

    ngOnInit() {
        this.loadUsers();
        this.loadPosts();
        this.loadReportedUsers();
        this.loadReportedPosts();
    }

    loadMoreUsers() {
        if (this.hasMoreUsers()) {
            this.usersOffset.update(o => o + 10);
            this.loadUsers(true);
        }
    }

    loadUsers(isLoadMore: boolean = false) {
        // We need to update AdminService to accept offset/limit. 
        // Currently getAllUsers() calls 'admin/get-users' with no params.
        // But backend accepts offset/limit.
        // Wait, AdminService.ts (Step 3553) getAllUsers() has NO params.
        // I need to update AdminService.ts FIRST.
        // For now, I'll pass params manually if I can, but I can't change AdminService call here without changing the service method signature.
        // Proceeding to update AdminService.ts in next step.
        this.adminService.getAllUsers(this.usersOffset()).subscribe({
            next: (data) => {
                if (data.length < 10) {
                    this.hasMoreUsers.set(false);
                } else {
                    this.hasMoreUsers.set(true);
                }

                if (isLoadMore) {
                    this.users.update(current => [...current, ...data]);
                } else {
                    this.users.set(data);
                }
            },
            error: (err) => console.error(err)
        });
    }

    loadMorePosts() {
        if (this.hasMorePosts()) {
            this.postsOffset.update(o => o + 10);
            this.loadPosts(true);
        }
    }

    loadPosts(isLoadMore: boolean = false) {
        this.postService.getAllPosts(this.postsOffset()).subscribe({
            next: (data) => {
                if (data.length < 10) {
                    this.hasMorePosts.set(false);
                } else {
                    this.hasMorePosts.set(true);
                }

                if (isLoadMore) {
                    this.posts.update(current => [...current, ...data]);
                } else {
                    this.posts.set(data);
                }
            },
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
                error: (err) => console.error('Failed to delete user', err)
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
                error: (err) => console.error('Failed to delete post', err)
            });
        }
    }

    banUser(userId: string) {
        if (confirm('Are you sure you want to ban/unban this user?')) {
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

    dismissReport(id: string, type: 'USER' | 'POST') {
        const message = type === 'USER'
            ? 'Are you sure you want to dismiss reports? This will also UNBAN the user if they were banned.'
            : 'Are you sure you want to dismiss reports for this post?';

        if (confirm(message)) {
            this.adminService.deleteReports(id, type).subscribe({
                next: () => {
                    if (type === 'USER') {
                        this.loadReportedUsers();
                    } else {
                        this.loadReportedPosts();
                    }
                },
                error: (err) => console.error('Failed to dismiss reports', err)
            });
        }
    }
}
