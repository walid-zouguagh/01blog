import { Component, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ProfileService } from './services/profile.service';
import { PostService, Post } from '../../features/posts/services/post.service';
import { User } from '../../shared/models/user.model';
import { PostCardComponent } from '../../features/posts/post-card/post-card.component';
import { AuthService } from '../../core/services/auth.service';
import { ReportDialogComponent } from '../reports/report-dialog/report-dialog.component';

@Component({
    selector: 'app-profile',
    standalone: true,
    imports: [CommonModule, MatButtonModule, MatIconModule, MatTabsModule, MatProgressSpinnerModule, PostCardComponent, MatDialogModule],
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.css']
})
export class ProfileComponent {
    user = signal<User | null>(null);
    posts = signal<Post[]>([]);
    loading = signal(true);
    offset = signal(0);
    hasMore = signal(true);
    isCurrentUser = signal(false);

    private dialog = inject(MatDialog);

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private profileService: ProfileService,
        private postService: PostService,
        private authService: AuthService
    ) { }

    openReportDialog() {
        const u = this.user();
        if (u) {
            this.dialog.open(ReportDialogComponent, {
                data: { type: 'USER', targetId: u.id },
                width: '500px'
            });
        }
    }

    getProfileImageUrl(url: string | undefined): string {
        if (!url) return 'assets/avatar-placeholder.png';
        if (url.startsWith('http')) return url;
        return `http://localhost:8080${url}`;
    }

    ngOnInit() {
        this.route.paramMap.subscribe(params => {
            const id = params.get('id');
            if (id) {
                this.loadProfile(id);
            } else {
                // Fallback to current user if just /profile route
                this.loadCurrentUser();
            }
        });

        // Check if route is exactly /profile
        if (this.router.url === '/profile') {
            this.loadCurrentUser();
        }
    }

    loadCurrentUser() {
        this.offset.set(0);
        this.hasMore.set(true);
        this.posts.set([]);
        this.profileService.getCurrentUser().subscribe({
            next: (user) => {
                this.user.set(user);
                this.isCurrentUser.set(true);
                this.loadUserPosts(user.id);
            },
            error: (err) => {
                console.error('Error loading current user', err);
                this.loading.set(false);
            }
        });
    }

    loadProfile(id: string) {
        this.offset.set(0);
        this.hasMore.set(true);
        this.posts.set([]);
        this.profileService.getProfile(id).subscribe({
            next: (user) => {
                this.user.set(user);
                this.isCurrentUser.set(user.myAccount); // Backend field
                this.loadUserPosts(id);
            },
            error: (err) => console.error(err)
        });
    }

    loadMore() {
        const u = this.user();
        if (u && !this.loading() && this.hasMore()) {
            this.offset.update(o => o + 10);
            this.loadUserPosts(u.id, true);
        }
    }

    loadUserPosts(userId: string, isLoadMore: boolean = false) {
        this.loading.set(true);
        this.postService.getUserPosts(userId, this.offset()).subscribe({
            next: (data) => {
                if (data.length < 10) {
                    this.hasMore.set(false);
                } else {
                    this.hasMore.set(true);
                }

                if (isLoadMore) {
                    this.posts.update(current => [...current, ...data]);
                } else {
                    this.posts.set(data);
                }
                this.loading.set(false);
            },
            error: (err) => {
                console.error(err);
                this.loading.set(false);
            }
        });
    }

    toggleFollow() {
        const u = this.user();
        if (u && !u.myAccount) {
            this.profileService.followUser(u.id).subscribe({
                next: (res) => {
                    // Update local state based on result
                    // Ideally backend returns updated status, but for now we toggle
                    this.user.update(currentUser => {
                        if (!currentUser) return null;
                        return {
                            ...currentUser,
                            hasConnect: !currentUser.hasConnect,
                            followers: currentUser.hasConnect ? currentUser.followers - 1 : currentUser.followers + 1
                        };
                    });
                },
                error: (err) => console.error(err)
            });
        }
    }

    isAdmin(): boolean {
        const currentUser = this.authService.currentUser();
        return currentUser?.role === 'ADMIN';
    }
}
