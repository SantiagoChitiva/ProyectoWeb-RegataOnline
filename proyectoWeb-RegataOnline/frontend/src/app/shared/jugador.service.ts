import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Jugador } from '../model/jugador';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class JugadorService {
  http = inject(HttpClient);

  findAll(): Observable<Jugador[]> {
    return this.http.get<Jugador[]>(`${environment.baseUrl}/jugador/list`);
  }

  findById(id: number): Observable<Jugador> {
    return this.http.get<Jugador>(`${environment.baseUrl}/jugador/${id}`);
  }

  update(jugador: Jugador): Observable<Jugador> {
    return this.http.put<Jugador>(`${environment.baseUrl}/jugador`,
       jugador,
       { 
        headers: new HttpHeaders({ "Content-Type": "application/json" })
       });
  }
/*
  create(jugador: Jugador): Observable<Jugador> {
    return this.http.post<Jugador>(`${environment.baseUrl}/jugador`,
      jugador, 
      {
        headers: new HttpHeaders({ "Content-Type": "application/json" })
      });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${environment.baseUrl}/jugador/${id}`);
  }
*/
}
