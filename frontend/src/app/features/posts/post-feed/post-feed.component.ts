import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostService, Post } from '../services/post.service';
import { PostCardComponent } from '../post-card/post-card.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTabsModule } from '@angular/material/tabs';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-post-feed',
  standalone: true,
  imports: [CommonModule, PostCardComponent, MatProgressSpinnerModule, MatTabsModule, MatButtonModule],
  template: `
    <div class="feed-container">
      <div class="feed-tabs glass-panel">
          <button mat-button [class.active]="activeTab() === 'following'" (click)="switchTab('following')">Following</button>
          <button mat-button [class.active]="activeTab() === 'global'" (click)="switchTab('global')">Global</button>
      </div>

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
            <p *ngIf="activeTab() === 'following'">Subscribe to users to see their posts here, or check the Global feed!</p>
            <p *ngIf="activeTab() === 'global'">Be the first to post something!</p>
          </div>
        }
      }
      
      @if (posts().length > 0 && hasMore()) {
        <div class="load-more-container">
            <button mat-stroked-button (click)="loadMore()" [disabled]="loading()">
                {{ loading() ? 'Loading...' : 'Load More' }}
            </button>
        </div>
      }
    </div>
  `,
  styles: [`
    .feed-container {
      max-width: 700px;
      margin: 0 auto;
      padding-top: 20px;
      padding-bottom: 40px;
    }
    .feed-tabs {
        display: flex;
        justify-content: center;
        margin-bottom: 20px;
        padding: 10px;
        gap: 10px;
    }
    .feed-tabs button {
        opacity: 0.7;
    }
    .feed-tabs button.active {
        opacity: 1;
        background: rgba(255,255,255,0.1);
        border-bottom: 2px solid var(--accent-color);
    }
    .spinner-container {
      display: flex;
      justify-content: center;
      padding: 40px;
    }
    .load-more-container {
        display: flex;
        justify-content: center;
        margin-top: 20px;
        margin-bottom: 20px;
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
  activeTab = signal<'following' | 'global'>('global');
  offset = signal(0);
  hasMore = signal(true);

  constructor(private postService: PostService) { }

  ngOnInit() {
    this.loadPosts();
  }

  switchTab(tab: 'following' | 'global') {
    this.activeTab.set(tab);
    this.offset.set(0);
    this.hasMore.set(true);
    this.posts.set([]); // Clear posts on tab switch
    this.loadPosts();
  }

  loadMore() {
    if (!this.loading() && this.hasMore()) {
      this.offset.update(o => o + 10);
      this.loadPosts(true);
    }
  }

  loadPosts(isLoadMore: boolean = false) {
    this.loading.set(true);
    let request;

    if (this.activeTab() === 'following') {
      request = this.postService.getFeed(this.offset());
    } else {
      request = this.postService.getAllPosts(this.offset());
    }

    request.subscribe({
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
        console.error('Failed to load posts', err);
        this.loading.set(false);
      }
    });
  }
}
