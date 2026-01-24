import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../../core/services/auth.service';

@Component({
    selector: 'app-register',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatIconModule, RouterLink],
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.css'] // Reusing login styles or separate? Let's use separate but content is similar.
})
export class RegisterComponent {
    registerForm: FormGroup;
    errorMessage = signal('');
    hidePassword = true;
    selectedFile: File | null = null;
    selectedFileName: string | null = null;
    imagePreview: string | null = null;

    constructor(private fb: FormBuilder, private auth: AuthService, private router: Router) {
        this.registerForm = this.fb.group({
            firstName: ['', [Validators.required]],
            lastName: ['', [Validators.required]],
            email: ['', [Validators.required, Validators.email]],
            userName: ['', [Validators.required]],
            password: ['', [Validators.required, Validators.minLength(6)]],
            bio: ['']
        });
    }

    onFileSelected(event: any) {
        const file = event.target.files[0];
        if (file) {
            this.selectedFile = file;
            this.selectedFileName = file.name;

            // Create preview
            const reader = new FileReader();
            reader.onload = () => {
                this.imagePreview = reader.result as string;
            };
            reader.readAsDataURL(file);
        }
    }

    onSubmit() {
        if (this.registerForm.valid) {
            this.errorMessage.set('');

            const formData = new FormData();
            Object.keys(this.registerForm.value).forEach(key => {
                const value = this.registerForm.value[key];
                if (value !== null && value !== undefined) {
                    formData.append(key, value);
                }
            });

            if (this.selectedFile) {
                formData.append('profileImage', this.selectedFile);
            }

            this.auth.register(formData).subscribe({
                next: () => {
                    this.router.navigate(['/']);
                },
                error: (err) => {
                    this.errorMessage.set('Registration failed. Please try again.');
                    console.error(err);
                }
            });
        }
    }
}
