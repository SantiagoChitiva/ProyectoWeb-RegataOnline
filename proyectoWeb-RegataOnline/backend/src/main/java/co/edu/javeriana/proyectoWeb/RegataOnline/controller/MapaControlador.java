package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.CrearMapaRequest;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.MapaDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Role;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.MapaServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/mapa")
@Secured({ Role.Code.ADMINISTRADOR })
@Tag(name = "Mapa", description = "Endpoints para gestionar los mapas del juego. Solo ADMINISTRADOR.")
public class MapaControlador {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MapaServicio mapaServicio;

    @PostMapping("/crear")
    @Operation(summary = "Crear nuevo mapa", description = "Crea un nuevo mapa con las celdas seleccionadas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Mapa creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<MapaDTO> crearMapa(
        @Parameter(description = "Datos para crear el mapa", required = true)
        @RequestBody CrearMapaRequest request) {
        try {
            log.info("Creando mapa: {}x{}", request.getFilas(), request.getColumnas());
            MapaDTO mapa = mapaServicio.crearMapa(request);
            log.info("Mapa creado exitosamente con ID: {}", mapa.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(mapa);
        } catch (Exception e) {
            log.error("Error al crear mapa: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/list")
    @Secured({ Role.Code.ADMINISTRADOR, Role.Code.JUGADOR })
    @Operation(summary = "Listar todos los mapas", description = "Obtiene una lista completa de todos los mapas creados")
    @ApiResponse(responseCode = "200", description = "Lista de mapas obtenida exitosamente")
    public List<MapaDTO> listarMapas() {
        log.info("Listando todos los mapas");
        List<MapaDTO> mapas = mapaServicio.listarMapas();
        log.info("Se encontraron {} mapas", mapas.size());
        return mapas;
    }

    @GetMapping("/{id}")
    @Secured({ Role.Code.ADMINISTRADOR, Role.Code.JUGADOR })
    @Operation(summary = "Obtener mapa por ID", description = "Obtiene los detalles de un mapa específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mapa encontrado"),
        @ApiResponse(responseCode = "404", description = "Mapa no encontrado")
    })
    public ResponseEntity<MapaDTO> obtenerMapa(
        @Parameter(description = "ID del mapa", example = "1", required = true)
        @PathVariable Long id) {
        return mapaServicio.buscarMapa(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un mapa", description = "Elimina un mapa del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mapa eliminado exitosamente"),
        @ApiResponse(responseCode = "400", description = "No se puede eliminar el mapa (puede tener barcos asociados)")
    })
    public ResponseEntity<String> borrarMapa(
        @Parameter(description = "ID del mapa a eliminar", example = "1", required = true)
        @PathVariable Long id) {
        try {
            log.info("Solicitud para eliminar mapa con ID: {}", id);
            mapaServicio.borrarMapa(id);
            return ResponseEntity.ok("Mapa eliminado exitosamente");
        } catch (Exception e) {
            log.error("Error al eliminar mapa {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
