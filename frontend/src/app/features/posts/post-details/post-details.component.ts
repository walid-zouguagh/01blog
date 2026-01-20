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

    ngOnInit() {
        const postId = this.route.snapshot.paramMap.get('id');
        if (postId) {
            this.loadPost(postId);
            this.loadComments(postId);
        } else {
            this.router.navigate(['/feed']);
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
            },
            error: (err) => console.error(err)
        });
    }
}
