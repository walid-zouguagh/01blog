import { Component, Input, Output, EventEmitter, inject, signal } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { Post, PostService } from '../services/post.service';
import { AuthService } from '../../../core/services/auth.service';
import { ReportDialogComponent } from '../../reports/report-dialog/report-dialog.component';

@Component({
    selector: 'app-post-card',
    standalone: true,
    imports: [CommonModule, MatCardModule, MatButtonModule, MatIconModule, MatMenuModule, MatDialogModule, DatePipe],
    templateUrl: './post-card.component.html',
    styleUrls: ['./post-card.component.css']
})
export class PostCardComponent {
    @Input({ required: true }) post!: Post;
    @Output() postDeleted = new EventEmitter<string>();

    liked = signal(false);
    likeCount = signal(0);

    private dialog = inject(MatDialog);
    private router = inject(Router);
    private postService = inject(PostService);
    public auth = inject(AuthService);

    ngOnInit() {
        this.liked.set(this.post.isLiked || false);
        this.likeCount.set(this.post.nbrOfLike || 0);
    }

    openReportDialog() {
        this.dialog.open(ReportDialogComponent, {
            data: { type: 'POST', targetId: this.post.id },
            width: '500px'
        });
    }

    toggleLike() {
        this.liked.update(v => !v);
        this.likeCount.update(c => this.liked() ? c + 1 : c - 1);
        // Call service to like/unlike (backend implementation required)
    }

    viewDetails() {
        this.router.navigate(['/post', this.post.id]);
    }

    deletePost() {
        if (confirm('Are you sure you want to delete this post?')) {
            this.postService.deletePost(this.post.id).subscribe({
                next: () => this.postDeleted.emit(this.post.id),
                error: (err) => alert('Failed to delete post')
            });
        }
    }

    isOwner(): boolean {
        const currentUser = this.auth.currentUser();
        return currentUser?.id === this.post.user.id;
    }
}
