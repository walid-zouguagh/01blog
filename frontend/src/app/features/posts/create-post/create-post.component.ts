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
    selectedFile: File | null = null;
    previewUrl: string | null = null;
    mediaType: 'IMAGE' | 'VIDEO' | null = null;
    isSubmitting = signal(false);

    isEditing = signal(false);
    postId: string | null = null;

    private route = inject(ActivatedRoute); // Use inject for consistency or constructor

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

    loadPostData(id: string) {
        this.postService.getPost(id).subscribe({
            next: (post) => {
                this.postForm.patchValue({
                    title: post.title,
                    content: post.content
                });
                // Handle media preview if needed
                if (post.media && post.media.length > 0) {
                    this.previewUrl = post.media[0].url;
                    this.mediaType = post.media[0].type;
                }
            },
            error: (err) => {
                console.error(err);
                this.router.navigate(['/feed']);
            }
        });
    }

    onFileSelected(event: any) {
        const file = event.target.files[0];
        if (file) {
            this.selectedFile = file;
            const reader = new FileReader();
            reader.onload = (e) => {
                this.previewUrl = e.target?.result as string;
            };
            reader.readAsDataURL(file);

            if (file.type.startsWith('image/')) {
                this.mediaType = 'IMAGE';
            } else if (file.type.startsWith('video/')) {
                this.mediaType = 'VIDEO';
            }
        }
    }

    onSubmit() {
        if (this.postForm.valid) {
            this.isSubmitting.set(true);

            const formData = new FormData();
            formData.append('title', this.postForm.get('title')?.value);
            formData.append('content', this.postForm.get('content')?.value);

            if (this.selectedFile) {
                formData.append('images', this.selectedFile);
            }

            if (this.isEditing() && this.postId) {
                // For edit, we might need to handle deleteImage logic or stick to current backend capabilities
                // Backend editPost expects PostDto.
                // It also checks ID.
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
