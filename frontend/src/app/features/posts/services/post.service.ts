import { Injectable, signal } from '@angular/core';
import { ApiService } from '../../../core/services/api.service';
import { Observable } from 'rxjs';
import { HttpParams } from '@angular/common/http';

export interface UserSummary {
    id: string; // UUID
    username: string;
    profilePicture?: string;
}

export interface Post {
    id: string; // UUID
    title: string;
    content: string; // was description in initial mock, backend says 'content'
    media?: { url: string, type: 'IMAGE' | 'VIDEO' }[]; // Backend PostDto has List<MediaDto>
    // Simplified for now based on what frontend used: urlMedia. 
    // Backend PostDto: List<MediaDto> media. 
    // Let's stick to what we see in Controller? 
    // Actually PostDto has: content, title, media (List), nbrOfLike, nbrOfComments, isLiked
    // We need to map this correctly.

    // For compatibility with existing components (temporarily):
    description?: string; // alias for content?
    urlMedia?: string;
    typeMedia?: 'IMAGE' | 'VIDEO';

    // Correct fields
    user: UserSummary;
    nbrOfLike: number;
    nbrOfComments: number;
    isLiked: boolean;
    createdAt: string;
}

@Injectable({
    providedIn: 'root'
})
export class PostService {

    constructor(private api: ApiService) { }

    getFeed(offset: number = 0): Observable<Post[]> {
        return this.api.get<Post[]>('subscribe-posts', new HttpParams().set('offset', offset));
    }

    getAllPosts(offset: number = 0): Observable<Post[]> {
        return this.api.get<Post[]>('posts', new HttpParams().set('offset', offset));
    }

    createPost(data: FormData): Observable<Post> {
        return this.api.postMedia<Post>('create_post', data);
    }

    editPost(data: FormData): Observable<Post> {
        return this.api.put('edit_post', data); // ApiService needs putMedia? Or generic put support FormData?
        // ApiService.put uses http.put(..., body). Angular HttpClient handles FormData if passed as body.
    }

    deletePost(postId: string): Observable<any> {
        return this.api.delete('delete_post', new HttpParams().set('postId', postId));
    }

    getUserPosts(userId: string, offset: number = 0): Observable<Post[]> {
        const params = new HttpParams()
            .set('idUserProfile', userId)
            .set('offset', offset);
        return this.api.get<Post[]>('user_post', params);
    }

    getPost(postId: string): Observable<Post> {
        return this.api.get<Post>('post', new HttpParams().set('postId', postId));
    }
}
