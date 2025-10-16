import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Celda } from '../model/celda';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class CeldaService {
  http = inject(HttpClient);

  findAll(): Observable<Celda[]> {
    return this.http.get<Celda[]>(`${environment.baseUrl}/celda/list`);
  }

  findById(id: number): Observable<Celda> {
    return this.http.get<Celda>(`${environment.baseUrl}/celda/${id}`);
  }
}
