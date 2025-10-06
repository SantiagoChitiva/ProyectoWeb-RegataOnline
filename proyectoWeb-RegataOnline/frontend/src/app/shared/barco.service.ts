import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Barco } from '../model/barco';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class BarcoService {
  http = inject(HttpClient);

  findAll(): Observable<Barco[]> {
    return this.http.get<Barco[]>(`${environment.baseUrl}/barco/list`);
  }

  findById(id: number): Observable<Barco> {
    return this.http.get<Barco>(`${environment.baseUrl}/barco/${id}`);
  }

  update(barco: Barco): Observable<Barco> {
    return this.http.put<Barco>(`${environment.baseUrl}/barco`,
       barco,
       { 
        headers: new HttpHeaders({ "Content-Type": "application/json" })
       });
  }
/*
  create(barco: Barco): Observable<Barco> {
    return this.http.post<Barco>(`${environment.baseUrl}/barco`,
      barco, 
      {
        headers: new HttpHeaders({ "Content-Type": "application/json" })
      });
  }
*/

  
}
