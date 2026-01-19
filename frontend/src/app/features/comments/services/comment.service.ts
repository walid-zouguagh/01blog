import { Injectable } from '@angular/core';
import { ApiService } from '../../../core/services/api.service';
import { Observable } from 'rxjs';
import { User } from '../../../shared/models/user.model';
import { HttpParams } from '@angular/common/http';

export interface CommentDto {
    id: string; // UUID
    content: string;
    likeCount: number;
    createdAt?: string;
    user: User; // The author
    postId: string;
    likedByCurrentUser?: boolean;
}

export interface CreateCommentDto {
    content: string;
    postId: string;
}

@Injectable({
    providedIn: 'root'
})
export class CommentService {

    constructor(private api: ApiService) { }

    getComments(postId: string, offset: number = 0): Observable<CommentDto[]> {
        const params = new HttpParams()
            .set('postId', postId)
            .set('offset', offset.toString());
        return this.api.get<CommentDto[]>('get_comments', params);
    }

    createComment(comment: CreateCommentDto): Observable<CommentDto> {
        return this.api.post<CommentDto>('create_comment', comment);
    }

    deleteComment(commentId: string): Observable<any> {
        return this.api.delete(`comments/${commentId}`);
    }
}
