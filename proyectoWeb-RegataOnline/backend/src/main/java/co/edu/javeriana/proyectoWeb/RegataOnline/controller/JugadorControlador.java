package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.JugadorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Role;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.JugadorServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/jugador")
@Tag(name = "Jugador", description = "Endpoints para gestionar los jugadores")
public class JugadorControlador {
    @Autowired
    private JugadorServicio jugadorServicio;

    @GetMapping("/list")
    @Secured({ Role.Code.ADMINISTRADOR })
    @Operation(summary = "Listar todos los jugadores", description = "Obtiene una lista de todos los jugadores. Solo ADMINISTRADOR.")
    @ApiResponse(responseCode = "200", description = "Lista de jugadores obtenida exitosamente")
    public List<JugadorDTO> listarJugadores() {
        return jugadorServicio.listarJugadores();
    }

    @GetMapping("/{id}")
    @Secured({ Role.Code.ADMINISTRADOR, Role.Code.JUGADOR })
    @Operation(summary = "Buscar jugador por ID", description = "Obtiene los detalles de un jugador específico. ADMINISTRADOR puede ver cualquier jugador, JUGADOR solo puede ver su propio perfil.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Jugador encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Jugador no encontrado")
    })
    public Optional<JugadorDTO> buscarJugador(
        @Parameter(description = "ID del jugador", example = "1", required = true)
        @PathVariable("id") Long id) {
        return jugadorServicio.buscarJugador(id);
    }

    @PostMapping
    @Secured({ Role.Code.ADMINISTRADOR })
    @Operation(summary = "Crear un nuevo jugador", description = "Registra un nuevo jugador en el sistema. Solo ADMINISTRADOR.")
    @ApiResponse(responseCode = "200", description = "Jugador creado exitosamente")
    public void crearJugador(
        @Parameter(description = "Datos del jugador a crear", required = true)
        @RequestBody JugadorDTO jugadorDTO) {
        jugadorServicio.guardarJugador(jugadorDTO);
    }

    @PutMapping
    @Secured({ Role.Code.ADMINISTRADOR })
    @Operation(summary = "Actualizar un jugador existente", description = "Actualiza la información de un jugador existente. Solo ADMINISTRADOR.")
    @ApiResponse(responseCode = "200", description = "Jugador actualizado exitosamente")
    public void actualizarJugador(
        @Parameter(description = "Datos del jugador a actualizar", required = true)
        @RequestBody JugadorDTO jugadorDTO) {
        jugadorServicio.guardarJugador(jugadorDTO);
    }

    @DeleteMapping("/{id}")
    @Secured({ Role.Code.ADMINISTRADOR })
    @Operation(summary = "Eliminar un jugador", description = "Elimina un jugador del sistema. Solo ADMINISTRADOR.")
    @ApiResponse(responseCode = "200", description = "Jugador eliminado exitosamente")
    public void borrarJugador(
        @Parameter(description = "ID del jugador a eliminar", example = "1", required = true)
        @PathVariable long id) {
        jugadorServicio.borrarJugador(id);
    }
}

