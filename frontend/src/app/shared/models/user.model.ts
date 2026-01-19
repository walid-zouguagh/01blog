export interface User {
    id: string;
    userName: string;
    firstName: string;
    lastName: string;
    email: string;
    bio?: string;
    urlProfileImage?: string;
    followers: number;
    following: number;
    role: 'USER' | 'ADMIN';
    myAccount: boolean;
    hasConnect: boolean; // True if current user follows this profile
}
