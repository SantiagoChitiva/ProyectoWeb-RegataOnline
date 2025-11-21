package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.*;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Role;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.PartidaMultijugadorServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/partida-multijugador")
@Tag(name = "Partida Multijugador", description = "Endpoints para gestionar partidas multijugador (hasta 4 jugadores)")
public class PartidaMultijugadorControlador {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PartidaMultijugadorServicio partidaServicio;

    @Secured({ Role.Code.ADMINISTRADOR, Role.Code.JUGADOR })
    @PostMapping("/crear")
    @Operation(summary = "Crear partida multijugador", description = "Crea una nueva partida multijugador. El creador es agregado como primer jugador. ADMIN y JUGADOR.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Partida creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o error en la creación")
    })
    public ResponseEntity<?> crearPartida(
        @Parameter(description = "Datos para crear la partida", required = true)
        @RequestBody CrearPartidaRequest request) {
        try {
            log.info("Solicitud crear partida multijugador: jugadorId={}, mapaId={}, barcoId={}", 
                request.getJugadorId(), request.getMapaId(), request.getBarcoId());
            
            PartidaDTO partida = partidaServicio.crearPartida(request);
            return ResponseEntity.ok(partida);
        } catch (Exception e) {
            log.error("Error al crear partida multijugador: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Secured({ Role.Code.JUGADOR })
    @PostMapping("/{id}/unirse")
    @Operation(summary = "Unirse a partida", description = "Permite a un jugador unirse a una partida existente (máximo 4 jugadores). Solo JUGADOR.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Jugador unido exitosamente"),
        @ApiResponse(responseCode = "400", description = "Partida llena, ya en curso, o jugador ya en partida")
    })
    public ResponseEntity<?> unirseAPartida(
        @Parameter(description = "ID de la partida", example = "1", required = true)
        @PathVariable Long id, 
        @RequestBody UnirsePartidaRequest request) {
        try {
            log.info("Solicitud unirse a partida {}: jugadorId={}", id, request.getJugadorId());
            request.setPartidaId(id);
            PartidaDTO partida = partidaServicio.unirseAPartida(request);
            return ResponseEntity.ok(partida);
        } catch (Exception e) {
            log.error("Error al unirse a partida {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @Secured({ Role.Code.JUGADOR })
    @PostMapping("/{id}/iniciar")
    @Operation(summary = "Iniciar partida", description = "Inicia la partida cuando hay al menos 2 jugadores. Solo JUGADOR (normalmente el creador).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Partida iniciada exitosamente"),
        @ApiResponse(responseCode = "400", description = "No hay suficientes jugadores (mínimo 2)")
    })
    public ResponseEntity<?> iniciarPartida(
        @Parameter(description = "ID de la partida", example = "1", required = true)
        @PathVariable Long id) {
        try {
            log.info("Solicitud iniciar partida {}", id);
            PartidaDTO partida = partidaServicio.iniciarPartida(id);
            return ResponseEntity.ok(partida);
        } catch (Exception e) {
            log.error("Error al iniciar partida {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    
    @Secured({ Role.Code.JUGADOR })
    @PostMapping("/{id}/mover")
    @Operation(summary = "Realizar movimiento", description = "Realiza un movimiento en tu turno. Acelera tu barco (-1, 0, +1) y calcula nueva posición. Solo JUGADOR.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Movimiento realizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "No es tu turno, aceleración inválida, o fuera del mapa")
    })
    public ResponseEntity<?> realizarMovimiento(
        @Parameter(description = "ID de la partida", example = "1", required = true)
        @PathVariable Long id, 
        @RequestBody RealizarMovimientoRequest request) {
        try {
            log.info("Solicitud movimiento en partida {}: jugadorId={}, ax={}, ay={}", 
                id, request.getJugadorId(), request.getAceleracionX(), request.getAceleracionY());
            request.setPartidaId(id);
            MovimientoDTO movimiento = partidaServicio.realizarMovimiento(request);
            return ResponseEntity.ok(movimiento);
        } catch (Exception e) {
            log.error("Error al realizar movimiento en partida {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

   
    @Secured({ Role.Code.ADMINISTRADOR, Role.Code.JUGADOR })
    @GetMapping("/{id}")
    @Operation(summary = "Obtener estado de partida", description = "Obtiene el estado completo de una partida: jugadores, turnos, posiciones. ADMIN y JUGADOR.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado de partida obtenido"),
        @ApiResponse(responseCode = "404", description = "Partida no encontrada")
    })
    public ResponseEntity<?> obtenerPartida(
        @Parameter(description = "ID de la partida", example = "1", required = true)
        @PathVariable Long id) {
        try {
            PartidaDTO partida = partidaServicio.obtenerPartida(id);
            return ResponseEntity.ok(partida);
        } catch (Exception e) {
            log.error("Partida {} no encontrada", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    
    @Secured({ Role.Code.JUGADOR })
    @GetMapping("/disponibles")
    @Operation(summary = "Listar partidas disponibles", description = "Lista todas las partidas en estado 'esperando' con espacio disponible. Solo JUGADOR.")
    @ApiResponse(responseCode = "200", description = "Lista de partidas disponibles")
    public ResponseEntity<List<PartidaDTO>> listarPartidasDisponibles() {
        List<PartidaDTO> partidas = partidaServicio.listarPartidasDisponibles();
        return ResponseEntity.ok(partidas);
    }

    
    @Secured({ Role.Code.ADMINISTRADOR, Role.Code.JUGADOR })
    @GetMapping("/{id}/movimientos/{jugadorId}")
    @Operation(summary = "Obtener historial de movimientos", description = "Obtiene todos los movimientos de un jugador en una partida. ADMIN y JUGADOR.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial de movimientos obtenido"),
        @ApiResponse(responseCode = "404", description = "Jugador no encontrado en esta partida")
    })
    public ResponseEntity<?> obtenerMovimientosJugador(
        @Parameter(description = "ID de la partida", example = "1", required = true)
        @PathVariable Long id, 
        @Parameter(description = "ID del jugador", example = "1", required = true)
        @PathVariable Long jugadorId) {
        try {
            List<MovimientoDTO> movimientos = partidaServicio.obtenerMovimientosJugador(id, jugadorId);
            return ResponseEntity.ok(movimientos);
        } catch (Exception e) {
            log.error("Error obteniendo movimientos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
