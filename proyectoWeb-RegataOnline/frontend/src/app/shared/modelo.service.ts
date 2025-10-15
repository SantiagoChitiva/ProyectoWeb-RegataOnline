import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Modelo } from '../model/modelo';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class ModeloService {
  http = inject(HttpClient);

  findAll(): Observable<Modelo[]> {
    return this.http.get<Modelo[]>(`${environment.baseUrl}/modelo/list`);
  }

  findById(id: number): Observable<Modelo> {
    return this.http.get<Modelo>(`${environment.baseUrl}/modelo/${id}`);
  }

  update(modelo:Modelo): Observable<Modelo> {
    return this.http.put<Modelo>(`${environment.baseUrl}/modelo`,
       modelo,
       { 
        headers: new HttpHeaders({ "Content-Type": "application/json" })
       });
  }

  create(modelo:Modelo): Observable<Modelo> {
    return this.http.post<Modelo>(`${environment.baseUrl}/modelo`,
      modelo, 
      {
        headers: new HttpHeaders({ "Content-Type": "application/json" })
      });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${environment.baseUrl}/modelo/${id}`);
  }
}
