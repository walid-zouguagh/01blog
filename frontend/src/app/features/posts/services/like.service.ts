import { Injectable } from '@angular/core';
import { ApiService } from '../../../core/services/api.service';
import { Observable } from 'rxjs';
import { HttpParams } from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class LikeService {

    constructor(private api: ApiService) { }

    like(postId: string): Observable<any> {
        return this.api.post('like', {}, new HttpParams().set('postId', postId));
    }
}
