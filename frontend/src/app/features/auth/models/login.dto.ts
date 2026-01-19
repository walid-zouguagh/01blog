export interface LoginDto {
    email?: string;
    username?: string; // allow login with email or username if backend supports it
    password?: string;
}
