import { HttpClient } from '@angular/common/http';
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
}
