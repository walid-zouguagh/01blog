import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { PostFeedComponent } from './features/posts/post-feed/post-feed.component';
import { authGuard } from './core/guards/auth.guard';
import { adminGuard } from './core/guards/admin.guard';
import { CreatePostComponent } from './features/posts/create-post/create-post.component';

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    {
        path: '',
        component: MainLayoutComponent,
        canActivate: [authGuard],
        children: [
            { path: '', redirectTo: 'feed', pathMatch: 'full' },
            { path: 'feed', loadComponent: () => import('./features/posts/post-feed/post-feed.component').then(m => m.PostFeedComponent) },
            { path: 'post/:id', loadComponent: () => import('./features/posts/post-details/post-details.component').then(m => m.PostDetailsComponent) },
            { path: 'create-post', loadComponent: () => import('./features/posts/create-post/create-post.component').then(m => m.CreatePostComponent) },
            { path: 'edit-post/:id', loadComponent: () => import('./features/posts/create-post/create-post.component').then(m => m.CreatePostComponent) },
            { path: 'profile', loadComponent: () => import('./features/profile/profile.component').then(m => m.ProfileComponent) },
            { path: 'profile/:id', loadComponent: () => import('./features/profile/profile.component').then(m => m.ProfileComponent) },
            { path: 'search', loadComponent: () => import('./features/search-users/search-users').then(m => m.SearchUsers) },
            {
                path: 'admin',
                canActivate: [adminGuard],
                loadComponent: () => import('./features/admin/admin-dashboard/admin-dashboard.component').then(m => m.AdminDashboardComponent)
            }
        ]
    },
    { path: '**', redirectTo: '' }
];
