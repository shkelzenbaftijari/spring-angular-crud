export interface UserResponse {
  id: number;
  login: string;
  firstName: string;
  lastName: string;
  email: string;
  activated: boolean;
  roles: string[];
}

export interface RegisterUserRequest {
  login: string;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  role: string;
}
