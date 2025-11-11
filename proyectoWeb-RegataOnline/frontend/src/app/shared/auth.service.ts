import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { LoginDto } from '../dto/login-dto';
import { SignupDto } from '../dto/signup-dto';
import { map, Observable } from 'rxjs';
import { JwtAuthenticationResponse } from '../dto/jwt-authentication-response';
import { environment } from '../../environments/environment';

const JWT_TOKEN = 'jwt-token';
const EMAIL = 'user-email';
const ROLE = 'user-role';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  http = inject(HttpClient);
  
  // Signal para manejar el estado de autenticación de forma reactiva
  private _isAuthenticated = signal(this.checkAuthentication());
  
  // Exponemos el signal como readonly
  isAuthenticatedSignal = this._isAuthenticated.asReadonly();
  
  private checkAuthentication(): boolean {
    return sessionStorage.getItem(JWT_TOKEN) != null;
  }


  login(loginDto: LoginDto): Observable<JwtAuthenticationResponse> {
    return this.http
      .post<JwtAuthenticationResponse>(
        `${environment.baseUrl}/auth/login`,
        loginDto
      )
      .pipe(
        map((jwt) => {
          // Importante: https://stackoverflow.com/questions/27067251/where-to-store-jwt-in-browser-how-to-protect-against-csrf
          sessionStorage.setItem(JWT_TOKEN, jwt.token); //localStorage
          sessionStorage.setItem(EMAIL, jwt.email);
          sessionStorage.setItem(ROLE, jwt.role);
          // Actualizamos el signal después de guardar el token
          this._isAuthenticated.set(true);
          return jwt;
        })
      );
  }

  signup(signupDto: Omit<SignupDto, 'confirmPassword'>): Observable<JwtAuthenticationResponse> {
    return this.http
      .post<JwtAuthenticationResponse>(
        `${environment.baseUrl}/auth/signup`,
        signupDto
      )
      .pipe(
        map((jwt) => {
          sessionStorage.setItem(JWT_TOKEN, jwt.token);
          sessionStorage.setItem(EMAIL, jwt.email);
          sessionStorage.setItem(ROLE, jwt.role);
          // Actualizamos el signal después de registrarse
          this._isAuthenticated.set(true);
          return jwt;
        })
      );
  }

  logout() {
    sessionStorage.removeItem(JWT_TOKEN);
    sessionStorage.removeItem(EMAIL);
    sessionStorage.removeItem(ROLE);
    // Actualizamos el signal después de limpiar el token
    this._isAuthenticated.set(false);
  }

  isAuthenticated() {
    return sessionStorage.getItem(JWT_TOKEN) != null;
  }

  token() {
    return sessionStorage.getItem(JWT_TOKEN);
  }

  role() {
    return sessionStorage.getItem(ROLE);
  }

  email() {
    return sessionStorage.getItem(EMAIL);
  }

  getCurrentUser(): Observable<any> {
    return this.http.get<any>(`${environment.baseUrl}/api/user/me`);
  }
}
