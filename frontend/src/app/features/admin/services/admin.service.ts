import { Injectable } from '@angular/core';
import { ApiService } from '../../../core/services/api.service';
import { Observable } from 'rxjs';
import { User } from '../../../shared/models/user.model';
import { Post } from '../../posts/services/post.service';
import { HttpParams } from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class AdminService {

    constructor(private api: ApiService) { }

    getAllUsers(offset: number = 0): Observable<User[]> {
        return this.api.get<User[]>('admin/get-users', new HttpParams().set('offset', offset));
    }

    deleteUser(userId: string): Observable<any> {
        const params = new HttpParams().set('userId', userId);
        return this.api.delete('admin/delete-user', params);
    }

    deletePost(postId: string): Observable<any> {
        const params = new HttpParams().set('postId', postId); // Correct Param Name? PostController deletePost uses defaultValue="0", name="postId".
        // Wait, PostController has @RequestParam(defaultValue = "0", name = "postId") UUID postId.
        // So 'postId' is correct.
        return this.api.delete('delete_post', params);
    }

    // Reports
    getReportedUsers(): Observable<any[]> {
        const params = new HttpParams().set('type', 'USER');
        return this.api.get('admin/reported', params);
    }

    getReportedPosts(): Observable<any[]> {
        const params = new HttpParams().set('type', 'POST');
        return this.api.get('admin/reported', params);
    }

    getReportReasonsUser(userId: string): Observable<any[]> {
        const params = new HttpParams().set('userId', userId);
        return this.api.get('admin/reason/user', params);
    }

    getReportReasonsPost(postId: string): Observable<any[]> {
        const params = new HttpParams().set('postId', postId);
        return this.api.get('admin/reason/post', params);
    }

    banUser(userId: string): Observable<boolean> {
        const params = new HttpParams().set('userId', userId);
        return this.api.put('admin/banne-user', {}, params); // Ensure ApiService put supports params (it doesn't yet)
    }
}
