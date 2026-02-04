import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { PostService } from '../services/post.service';
import { Router, ActivatedRoute } from '@angular/router';
import { inject } from '@angular/core';

@Component({
    selector: 'app-create-post',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatIconModule],
    templateUrl: './create-post.component.html',
    styleUrls: ['./create-post.component.css']
})
export class CreatePostComponent {
    postForm: FormGroup;
    selectedFiles: File[] = [];
    previews: { url: string, type: 'IMAGE' | 'VIDEO', file?: File, originalUrl?: string }[] = [];
    imagesToDelete: string[] = [];
    isSubmitting = signal(false);

    isEditing = signal(false);
    postId: string | null = null;

    private route = inject(ActivatedRoute);

    constructor(private fb: FormBuilder, private postService: PostService, private router: Router, private activatedRoute: ActivatedRoute) {
        this.postForm = this.fb.group({
            title: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
            content: ['', [Validators.required, Validators.minLength(3)]]
        });
    }

    ngOnInit() {
        this.postId = this.activatedRoute.snapshot.paramMap.get('id');
        if (this.postId) {
            this.isEditing.set(true);
            this.loadPostData(this.postId);
        }
    }

    getMediaUrl(url: string | undefined): string {
        if (!url) return '';
        if (url.startsWith('http')) return url;
        return `http://localhost:8080${url}`;
    }

    loadPostData(id: string) {
        this.postService.getPost(id).subscribe({
            next: (post) => {
                this.postForm.patchValue({
                    title: post.title,
                    content: post.content
                });

                if (post.media && post.media.length > 0) {
                    this.previews = post.media.map(m => ({
                        url: this.getMediaUrl(m.url),
                        type: m.type,
                        originalUrl: m.url // Store relative path for deletion
                    }));
                }
            },
            error: (err) => {
                console.error(err);
                this.router.navigate(['/feed']);
            }
        });
    }

    onFileSelected(event: any) {
        const files: FileList = event.target.files;
        if (files) {
            const newFiles = Array.from(files);
            // Check count
            if (this.previews.length + newFiles.length > 5) {
                alert('Maximum 5 files allowed');
                return;
            }

            // Append new files
            this.selectedFiles = [...this.selectedFiles, ...newFiles];

            // Generate previews for new files
            newFiles.forEach(file => {
                const reader = new FileReader();
                reader.onload = (e) => {
                    const type = file.type.startsWith('image/') ? 'IMAGE' : 'VIDEO';
                    this.previews.push({
                        url: e.target?.result as string,
                        type: type as 'IMAGE' | 'VIDEO',
                        file: file
                    });
                };
                reader.readAsDataURL(file);
            });

            // Clear input value so same file can be selected again if needed
            event.target.value = '';
        }
    }

    removeMedia(index: number) {
        const itemToRemove = this.previews[index];
        this.previews.splice(index, 1);

        if (itemToRemove.file) {
            // It was a new file
            const fileIndex = this.selectedFiles.indexOf(itemToRemove.file);
            if (fileIndex > -1) {
                this.selectedFiles.splice(fileIndex, 1);
            }
        } else if (itemToRemove.originalUrl) {
            // It was an existing file -> Mark for deletion
            this.imagesToDelete.push(itemToRemove.originalUrl);
        }
    }

    onSubmit() {
        if (this.postForm.valid) {
            this.isSubmitting.set(true);

            const formData = new FormData();
            formData.append('title', this.postForm.get('title')?.value);
            formData.append('content', this.postForm.get('content')?.value);

            // Append all NEW selected files
            this.selectedFiles.forEach(file => {
                formData.append('images', file);
            });

            // Append deleted images list
            this.imagesToDelete.forEach(url => {
                formData.append('deleteImage', url);
            });

            if (this.isEditing() && this.postId) {
                formData.append('id', this.postId);

                this.postService.editPost(formData).subscribe({
                    next: () => {
                        this.isSubmitting.set(false);
                        this.router.navigate(['/post', this.postId]);
                    },
                    error: (err) => {
                        console.error(err);
                        this.isSubmitting.set(false);
                    }
                });
            } else {
                this.postService.createPost(formData).subscribe({
                    next: () => {
                        this.isSubmitting.set(false);
                        this.router.navigate(['/']);
                    },
                    error: (err) => {
                        console.error(err);
                        this.isSubmitting.set(false);
                    }
                });
            }
        }
    }
}
