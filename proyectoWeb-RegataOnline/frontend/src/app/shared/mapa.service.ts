import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "../../environments/environment";

export interface Celda {
  id?: number;
  tipo: string;
  posicionX: number;
  posicionY: number;
}

export interface Mapa {
  id?: number;
  filas: number;
  columnas: number;
  celdas?: Celda[];
}

export interface CrearMapaRequest {
  filas: number;
  columnas: number;
  celdasSeleccionadas: Celda[];
}

@Injectable({
  providedIn: 'root'
})
export class MapaService {
  http = inject(HttpClient);
  url = environment.baseUrl + '/mapa';

  crearMapa(request: CrearMapaRequest): Observable<Mapa> {
    return this.http.post<Mapa>(`${this.url}/crear`, request);
  }

  listarMapas(): Observable<Mapa[]> {
    return this.http.get<Mapa[]>(`${this.url}/list`);
  }

  buscarMapa(id: number): Observable<Mapa> {
    return this.http.get<Mapa>(`${this.url}/${id}`);
  }

  borrarMapa(id: number): Observable<string> {
    return this.http.delete(`${this.url}/${id}`, { responseType: 'text' });
  }
}
