import { Component, Inject, signal } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatListModule } from '@angular/material/list';

@Component({
    selector: 'app-report-reasons-dialog',
    standalone: true,
    imports: [CommonModule, MatDialogModule, MatButtonModule, MatListModule],
    template: `
    <h2 mat-dialog-title>Report Reasons</h2>
    <mat-dialog-content>
      <mat-list>
        @for (item of data.reasons; track $index) {
          <mat-list-item>
             <span matListItemTitle>{{item.registerDto.userName}}</span>
             <span matListItemLine>{{item.reason}}</span>
          </mat-list-item>
        } @empty {
            <p>No details found.</p>
        }
      </mat-list>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button mat-dialog-close>Close</button>
    </mat-dialog-actions>
  `
})
export class ReportReasonsDialogComponent {
    constructor(@Inject(MAT_DIALOG_DATA) public data: { reasons: any[] }) { }
}
