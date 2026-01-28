import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatCardModule } from '@angular/material/card';
import { ProfileService } from '../profile/services/profile.service';
import { RegisterDto } from '../../features/auth/models/register.dto';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-search-users',
  imports: [
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatListModule,
    MatCardModule,
    RouterLink
  ],
  templateUrl: './search-users.html',
  styleUrl: './search-users.css',
})
export class SearchUsers implements OnInit {
  private profileService = inject(ProfileService);

  searchQuery: string = '';
  users: any[] = []; // using any for now or RegisterDto if available

  ngOnInit() {
    this.search();
  }

  search() {
    // Removed trim check to allow empty search (find all)
    this.profileService.searchUsers(this.searchQuery).subscribe({
      next: (data) => {
        this.users = data;
      },
      error: (err) => console.error('Search error', err)
    });
  }

  toggleFollow(user: any) {
    if (!user.id) {
      return;
    }
    this.profileService.followUser(user.id).subscribe({
      next: (response) => {
        // Optimistic update using response from backend
        // response.isFollowing is the NEW state (true if strictly added, false if removed)
        user.hasConnect = response.isFollowing;
      },
      error: (err) => console.error('Follow error', err)
    });
  }
  getProfileImageUrl(url: string | undefined): string {
    if (!url) return 'assets/default-avatar.png';
    if (url.startsWith('http')) return url;
    return `http://localhost:8080${url}`;
  }
}
