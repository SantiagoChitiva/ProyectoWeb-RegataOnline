import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Barco } from '../model/barco';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BarcoService {
  baseUrl = "http://localhost:8080";

  http = inject(HttpClient);

  findAll(): Observable<Barco[]> {
    return this.http.get<Barco[]>(`${this.baseUrl}/barco/list`);
  }
  
}
