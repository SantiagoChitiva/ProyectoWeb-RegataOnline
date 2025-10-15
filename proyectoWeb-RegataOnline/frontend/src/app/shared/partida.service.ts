import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "../../environments/environment";

export interface Partida {
  id?: number;
  jugadorId: number;
  jugadorNombre?: string;
  mapaId: number;
  mapaFilas?: number;
  mapaColumnas?: number;
  barcoId: number;
  barcoNombre?: string;
  barcoPosicionX?: number;
  barcoPosicionY?: number;
  estado: string; // "activa", "pausada", "terminada"
  fechaInicio?: string;
  fechaUltimaJugada?: string;
  movimientos?: number;
  haLlegadoMeta?: boolean;
}

export interface CrearPartidaRequest {
  jugadorId: number;
  mapaId: number;
  barcoId: number;
}

export interface MoverBarcoRequest {
  x: number;
  y: number;
}

@Injectable({
  providedIn: 'root'
})
export class PartidaService {
  http = inject(HttpClient);
  url = environment.baseUrl + '/partida';

  crearPartida(request: CrearPartidaRequest): Observable<Partida> {
    return this.http.post<Partida>(`${this.url}/crear`, request);
  }

  obtenerPartidaActiva(jugadorId: number): Observable<Partida> {
    return this.http.get<Partida>(`${this.url}/activa/${jugadorId}`);
  }

  obtenerPartida(id: number): Observable<Partida> {
    return this.http.get<Partida>(`${this.url}/${id}`);
  }

  listarPartidasJugador(jugadorId: number): Observable<Partida[]> {
    return this.http.get<Partida[]>(`${this.url}/jugador/${jugadorId}`);
  }

  pausarPartida(id: number): Observable<Partida> {
    return this.http.put<Partida>(`${this.url}/${id}/pausar`, {});
  }

  finalizarPartida(id: number): Observable<Partida> {
    return this.http.put<Partida>(`${this.url}/${id}/finalizar`, {});
  }

  moverBarco(partidaId: number, x: number, y: number): Observable<Partida> {
    return this.http.put<Partida>(`${this.url}/${partidaId}/mover?x=${x}&y=${y}`, {});
  }
}
