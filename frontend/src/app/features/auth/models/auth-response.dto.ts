import { User } from '../../../shared/models/user.model';

export interface AuthResponse {
    token: string;
    user: User;
}
