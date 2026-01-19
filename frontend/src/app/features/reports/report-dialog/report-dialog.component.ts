import { Component, Inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReportService, ReportDto } from '../services/report.service';

export interface ReportDialogData {
    type: 'POST' | 'USER';
    targetId: string;
}

@Component({
    selector: 'app-report-dialog',
    standalone: true,
    imports: [CommonModule, MatDialogModule, MatButtonModule, MatFormFieldModule, MatInputModule, ReactiveFormsModule],
    template: `
    <h2 mat-dialog-title>Report {{data.type === 'POST' ? 'Post' : 'User'}}</h2>
    <mat-dialog-content>
      <form [formGroup]="form">
        <mat-form-field appearance="fill" class="full-width">
          <mat-label>Reason</mat-label>
          <textarea matInput formControlName="reason" rows="4" placeholder="Describe why you are reporting this..."></textarea>
          <mat-error *ngIf="form.get('reason')?.hasError('required')">Reason is required</mat-error>
          <mat-error *ngIf="form.get('reason')?.hasError('minlength')">Must be at least 10 characters</mat-error>
        </mat-form-field>
      </form>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button mat-dialog-close>Cancel</button>
      <button mat-raised-button color="warn" [disabled]="form.invalid || loading()" (click)="submit()">
        {{ loading() ? 'Sending...' : 'Report' }}
      </button>
    </mat-dialog-actions>
  `,
    styles: [`
    .full-width { width: 100%; }
  `]
})
export class ReportDialogComponent {
    form: FormGroup;
    loading = signal(false);

    constructor(
        private fb: FormBuilder,
        private reportService: ReportService,
        private dialogRef: MatDialogRef<ReportDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: ReportDialogData
    ) {
        this.form = this.fb.group({
            reason: ['', [Validators.required, Validators.minLength(10)]]
        });
    }

    submit() {
        if (this.form.invalid) return;
        this.loading.set(true);

        const report: ReportDto = {
            reason: this.form.value.reason,
            reportedPostId: this.data.type === 'POST' ? this.data.targetId : undefined,
            reportedUserId: this.data.type === 'USER' ? this.data.targetId : undefined
        };

        const request = this.data.type === 'POST'
            ? this.reportService.reportPost(report)
            : this.reportService.reportUser(report);

        request.subscribe({
            next: () => {
                this.loading.set(false);
                this.dialogRef.close(true);
            },
            error: (err) => {
                console.error(err);
                this.loading.set(false);
            }
        });
    }
}
