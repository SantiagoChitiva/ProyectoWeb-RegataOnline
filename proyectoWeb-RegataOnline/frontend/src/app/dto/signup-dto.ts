export interface SignupDto {
  nombre: string;
  email: string;
  password: string;
  confirmPassword?: string;
  role: string;
}
