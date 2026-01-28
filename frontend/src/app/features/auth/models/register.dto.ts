export interface RegisterDto {
    id?: string;
    firstName?: string;
    lastName?: string;
    userName?: string;
    email?: string;
    password?: string;
    bio?: string;
    urlProfileImage?: string;
    role?: string;
    followers?: number;
    following?: number;
    hasConnect?: boolean;
    myAccount?: boolean;
}
