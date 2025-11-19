export class PartidaMultijugador {
  public id?: number;
  public jugadorCreadorId?: number;
  public jugadorCreadorNombre?: string;
  public mapaId?: number;
  public mapaNombre?: string;
  public numeroTurnoActual?: number;
  public ordenTurnoActual?: number;
  public cantidadJugadores?: number;
  public maxJugadores?: number;
  public estado?: string; // "esperando" | "en_curso" | "terminada"
  public ganadorId?: number;
  public ganadorNombre?: string;
  public jugadores?: PartidaJugador[];
}

export class PartidaJugador {
  public id?: number;
  public partidaId?: number;
  public jugadorId?: number;
  public jugadorNombre?: string;
  public barcoId?: number;
  public barcoNombre?: string;
  public ordenTurno?: number;
  public posicionX?: number;
  public posicionY?: number;
  public velocidadX?: number;
  public velocidadY?: number;
  public estado?: string;
  public haLlegadoMeta?: boolean;
  public posicionFinal?: number;
  public movimientosRealizados?: number;
}

export class Movimiento {
  public id?: number;
  public partidaJugadorId?: number;
  public jugadorNombre?: string;
  public numeroTurno?: number;
  public aceleracionX?: number;
  public aceleracionY?: number;
  public velocidadAnteriorX?: number;
  public velocidadAnteriorY?: number;
  public velocidadNuevaX?: number;
  public velocidadNuevaY?: number;
  public posicionAnteriorX?: number;
  public posicionAnteriorY?: number;
  public posicionNuevaX?: number;
  public posicionNuevaY?: number;
  public llegoAMeta?: boolean;
  public chocoConPared?: boolean;
}

export class UnirsePartidaRequest {
  public jugadorId?: number;
  public barcoId?: number;
}

export class RealizarMovimientoRequest {
  public jugadorId?: number;
  public aceleracionX?: number;
  public aceleracionY?: number;
}
