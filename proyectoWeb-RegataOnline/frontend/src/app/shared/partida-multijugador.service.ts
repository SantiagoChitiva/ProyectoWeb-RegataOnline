import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.development';
import { PartidaMultijugador, UnirsePartidaRequest, RealizarMovimientoRequest, Movimiento } from '../model/partida-multijugador';

@Injectable({
  providedIn: 'root'
})
export class PartidaMultijugadorService {
  http = inject(HttpClient);

  crearPartida(jugadorId: number, mapaId: number, barcoId: number): Observable<PartidaMultijugador> {
    return this.http.post<PartidaMultijugador>(
      `${environment.baseUrl}/partida-multijugador/crear`,
      { jugadorId, mapaId, barcoId },
      { headers: new HttpHeaders({ "Content-Type": "application/json" }) }
    );
  }

  unirseAPartida(partidaId: number, request: UnirsePartidaRequest): Observable<PartidaMultijugador> {
    return this.http.post<PartidaMultijugador>(
      `${environment.baseUrl}/partida-multijugador/${partidaId}/unirse`,
      request,
      { headers: new HttpHeaders({ "Content-Type": "application/json" }) }
    );
  }

  iniciarPartida(partidaId: number): Observable<PartidaMultijugador> {
    return this.http.post<PartidaMultijugador>(
      `${environment.baseUrl}/partida-multijugador/${partidaId}/iniciar`,
      {},
      { headers: new HttpHeaders({ "Content-Type": "application/json" }) }
    );
  }

  realizarMovimiento(partidaId: number, request: RealizarMovimientoRequest): Observable<PartidaMultijugador> {
    return this.http.post<PartidaMultijugador>(
      `${environment.baseUrl}/partida-multijugador/${partidaId}/mover`,
      request,
      { headers: new HttpHeaders({ "Content-Type": "application/json" }) }
    );
  }

  obtenerPartida(partidaId: number): Observable<PartidaMultijugador> {
    return this.http.get<PartidaMultijugador>(
      `${environment.baseUrl}/partida-multijugador/${partidaId}`
    );
  }

  listarPartidasDisponibles(): Observable<PartidaMultijugador[]> {
    return this.http.get<PartidaMultijugador[]>(
      `${environment.baseUrl}/partida-multijugador/disponibles`
    );
  }

  obtenerMovimientosJugador(partidaId: number, jugadorId: number): Observable<Movimiento[]> {
    return this.http.get<Movimiento[]>(
      `${environment.baseUrl}/partida-multijugador/${partidaId}/movimientos/${jugadorId}`
    );
  }
}
