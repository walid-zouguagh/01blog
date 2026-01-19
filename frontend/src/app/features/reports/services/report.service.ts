import { Injectable } from '@angular/core';
import { ApiService } from '../../../core/services/api.service';
import { Observable } from 'rxjs';
import { HttpParams } from '@angular/common/http';

export interface ReportDto {
    reason: string;
    reportedPostId?: string;
    reportedUserId?: string;
}

@Injectable({
    providedIn: 'root'
})
export class ReportService {

    constructor(private api: ApiService) { }

    reportPost(report: ReportDto): Observable<void> {
        return this.api.post('report-post', report);
    }

    reportUser(report: ReportDto): Observable<void> {
        return this.api.post('report-user', report);
    }
}
