import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostService, Post } from '../services/post.service';
import { PostCardComponent } from '../post-card/post-card.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-post-feed',
  standalone: true,
  imports: [CommonModule, PostCardComponent, MatProgressSpinnerModule],
  template: `
    <div class="feed-container">
      @if (loading()) {
        <div class="spinner-container">
          <mat-spinner diameter="40"></mat-spinner>
        </div>
      }

      @for (post of posts(); track post.id) {
        <app-post-card [post]="post"></app-post-card>
      } @empty {
        @if (!loading()) {
          <div class="empty-state glass-panel">
            <h3>No posts yet</h3>
            <p>Subscribe to users to see their posts here!</p>
          </div>
        }
      }
    </div>
  `,
  styles: [`
    .feed-container {
      max-width: 700px;
      margin: 0 auto;
      padding-top: 20px;
    }
    .spinner-container {
      display: flex;
      justify-content: center;
      padding: 40px;
    }
    .empty-state {
      padding: 60px 20px;
      text-align: center;
      color: var(--text-secondary);
      background: rgba(255, 255, 255, 0.02);
      border: 1px solid var(--glass-border);
      border-radius: var(--border-radius-md);
      margin-top: 20px;
    }
    .empty-state h3 {
      font-size: 1.5rem;
      margin-bottom: 10px;
      color: var(--text-primary);
    }
  `]
})
export class PostFeedComponent {
  posts = signal<Post[]>([]);
  loading = signal(true);

  constructor(private postService: PostService) { }

  ngOnInit() {
    this.loadPosts();
  }

  loadPosts() {
    this.loading.set(true);
    // For demo/dev purposes, checking both feed sources or just subscribe-posts as per requirement
    this.postService.getFeed().subscribe({
      next: (data) => {
        this.posts.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Failed to load posts', err);
        // Fallback to all posts if subscribe is empty/error? Optional.
        this.loading.set(false);
      }
    });
  }
}
