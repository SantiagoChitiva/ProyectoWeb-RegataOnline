package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.CrearPartidaRequest;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.PartidaDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Role;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.PartidaServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/partida")
@Secured({ Role.Code.JUGADOR })
@Tag(name = "Partida", description = "Endpoints para que los JUGADORES gestionen sus partidas")
public class PartidaControlador {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PartidaServicio partidaServicio;

    @PostMapping("/crear")
    @Operation(summary = "Crear nueva partida", description = "Crea una nueva partida con jugador, mapa y barco. Solo JUGADOR.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Partida creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o jugador ya tiene partida activa")
    })
    public ResponseEntity<?> crearPartida(
        @Parameter(description = "Datos para crear la partida", required = true)
        @RequestBody CrearPartidaRequest request) {
        try {
            log.info("Solicitud para crear partida: jugadorId={}, mapaId={}, barcoId={}", 
                request.getJugadorId(), request.getMapaId(), request.getBarcoId());
            
            PartidaDTO partida = partidaServicio.crearPartida(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(partida);
        } catch (Exception e) {
            log.error("Error al crear partida: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/activa/{jugadorId}")
    @Operation(summary = "Obtener partida activa", description = "Busca la partida activa o pausada de un jugador. Solo JUGADOR.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Partida encontrada"),
        @ApiResponse(responseCode = "404", description = "No hay partida activa")
    })
    public ResponseEntity<PartidaDTO> obtenerPartidaActiva(
        @Parameter(description = "ID del jugador", example = "1", required = true)
        @PathVariable Long jugadorId) {
        return partidaServicio.buscarPartidaActiva(jugadorId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener partida por ID", description = "Obtiene los detalles de una partida específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Partida encontrada"),
        @ApiResponse(responseCode = "404", description = "Partida no encontrada")
    })
    public ResponseEntity<PartidaDTO> obtenerPartida(
        @Parameter(description = "ID de la partida", example = "1", required = true)
        @PathVariable Long id) {
        return partidaServicio.obtenerPartida(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/jugador/{jugadorId}")
    @Operation(summary = "Listar partidas del jugador", description = "Obtiene todas las partidas de un jugador")
    @ApiResponse(responseCode = "200", description = "Lista de partidas obtenida exitosamente")
    public List<PartidaDTO> listarPartidasJugador(
        @Parameter(description = "ID del jugador", example = "1", required = true)
        @PathVariable Long jugadorId) {
        return partidaServicio.listarPartidasJugador(jugadorId);
    }

    @PutMapping("/{id}/pausar")
    @Operation(summary = "Pausar partida", description = "Pausa una partida activa")
    @ApiResponse(responseCode = "200", description = "Partida pausada exitosamente")
    public ResponseEntity<PartidaDTO> pausarPartida(
        @Parameter(description = "ID de la partida", example = "1", required = true)
        @PathVariable Long id) {
        try {
            PartidaDTO partida = partidaServicio.pausarPartida(id);
            return ResponseEntity.ok(partida);
        } catch (Exception e) {
            log.error("Error al pausar partida: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}/finalizar")
    @Operation(summary = "Finalizar partida", description = "Marca una partida como terminada")
    @ApiResponse(responseCode = "200", description = "Partida finalizada exitosamente")
    public ResponseEntity<PartidaDTO> finalizarPartida(
        @Parameter(description = "ID de la partida", example = "1", required = true)
        @PathVariable Long id) {
        try {
            PartidaDTO partida = partidaServicio.finalizarPartida(id);
            return ResponseEntity.ok(partida);
        } catch (Exception e) {
            log.error("Error al finalizar partida: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}/mover")
    @Operation(summary = "Mover barco", description = "Mueve el barco aplicando aceleración (-1, 0, o +1) a cada componente de velocidad")
    @ApiResponse(responseCode = "200", description = "Barco movido exitosamente")
    public ResponseEntity<?> moverBarco(
        @Parameter(description = "ID de la partida", example = "1", required = true)
        @PathVariable Long id,
        @Parameter(description = "Aceleración en X (-1, 0, o +1)", required = true)
        @RequestParam Integer aceleracionX,
        @Parameter(description = "Aceleración en Y (-1, 0, o +1)", required = true)
        @RequestParam Integer aceleracionY) {
        try {
            PartidaDTO partida = partidaServicio.moverBarco(id, aceleracionX, aceleracionY);
            return ResponseEntity.ok(partida);
        } catch (Exception e) {
            log.error("Error al mover barco: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
