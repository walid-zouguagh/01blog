import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { PostService, Post } from '../services/post.service';
import { LikeService } from '../services/like.service';
import { CommentService, CommentDto } from '../../comments/services/comment.service';
import { AuthService } from '../../../core/services/auth.service';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
    selector: 'app-post-details',
    standalone: true,
    imports: [CommonModule, MatCardModule, MatButtonModule, MatIconModule, MatInputModule, MatFormFieldModule, ReactiveFormsModule, DatePipe],
    templateUrl: './post-details.component.html',
    styleUrls: ['./post-details.component.css']
})
export class PostDetailsComponent {
    post = signal<Post | null>(null);
    comments = signal<CommentDto[]>([]);
    loading = signal(true);

    private route = inject(ActivatedRoute);
    private router = inject(Router);
    private postService = inject(PostService);
    private commentService = inject(CommentService);
    private likeService = inject(LikeService);
    public auth = inject(AuthService);
    private fb = inject(FormBuilder);

    commentForm = this.fb.group({
        content: ['', [Validators.required, Validators.minLength(3)]]
    });

    getMediaUrl(url: string | undefined): string {
        if (!url) return '';
        if (url.startsWith('http')) return url;
        return `http://localhost:8080${url}`;
    }

    ngOnInit() {
        const postId = this.route.snapshot.paramMap.get('id');
        if (postId) {
            this.loadPost(postId);
            this.loadComments(postId);
        } else {
            this.router.navigate(['/feed']);
        }
    }

    currentSlide = signal(0);

    nextSlide(event: Event) {
        event.stopPropagation();
        const p = this.post();
        if (p && p.media && p.media.length > 1) {
            this.currentSlide.update(curr => (curr + 1) % p.media.length);
        }
    }

    prevSlide(event: Event) {
        event.stopPropagation();
        const p = this.post();
        if (p && p.media && p.media.length > 1) {
            this.currentSlide.update(curr => (curr - 1 + p.media.length) % p.media.length);
        }
    }

    loadPost(id: string) {
        this.postService.getPost(id).pipe(
            catchError(err => {
                console.error(err);
                this.router.navigate(['/feed']);
                return of(null);
            })
        ).subscribe(post => {
            if (post) this.post.set(post);
            this.loading.set(false);
        });
    }

    loadComments(postId: string) {
        this.commentService.getComments(postId).subscribe({
            next: (data) => this.comments.set(data),
            error: (err) => console.error(err)
        });
    }

    toggleLike() {
        const p = this.post();
        if (!p) return;

        // Optimistic update
        const wasLiked = p.isLiked;
        const newCount = wasLiked ? p.nbrOfLike - 1 : p.nbrOfLike + 1;

        this.post.update(current => current ? { ...current, isLiked: !wasLiked, nbrOfLike: newCount } : null);

        this.likeService.like(p.id).subscribe({
            // Backend returns updated state if needed, or just success
            error: (err) => {
                console.error(err);
                // Revert if error
                this.post.update(current => current ? { ...current, isLiked: wasLiked, nbrOfLike: p.nbrOfLike } : null);
            }
        });
    }

    submitComment() {
        if (this.commentForm.invalid || !this.post()) return;

        this.commentService.createComment({
            content: this.commentForm.value.content || '',
            postId: this.post()!.id
        }).subscribe({
            next: (newComment) => {
                this.comments.update(list => [newComment, ...list]);
                this.commentForm.reset();
                // Optionally update post comment count
                this.post.update(p => p ? { ...p, nbrOfComments: (p.nbrOfComments || 0) + 1 } : null);
            },
            error: (err) => console.error(err)
        });
    }

    deleteComment(commentId: string) {
        if (confirm('Are you sure you want to delete this comment?')) {
            this.commentService.deleteComment(commentId).subscribe({
                next: () => {
                    this.comments.update(list => list.filter(c => c.id !== commentId));
                    this.post.update(p => p ? { ...p, nbrOfComments: (p.nbrOfComments || 0) - 1 } : null);
                },
                error: (err) => console.error('Failed to delete comment', err)
            });
        }
    }

    isCommentOwnerOrAdmin(comment: CommentDto): boolean {
        const currentUser = this.auth.currentUser();
        if (!currentUser) return false;

        // Admin can delete any comment
        if (currentUser.role === 'ADMIN') return true;

        // User can delete their own comment
        return currentUser.id === comment.user.id;
    }
}
