package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.ErrorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Role;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.BarcoServicio;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.CeldaServicio;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.JugadorServicio;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.ModeloServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/barco")
@Secured({ Role.Code.ADMINISTRADOR })
@Tag(name = "Barco", description = "Endpoints para gestionar los barcos. Solo ADMINISTRADOR.") 
public class BarcoControlador {
    @Autowired 
    private BarcoServicio barcoServicio;
    @Autowired
    private ModeloServicio modeloServicio;
    @Autowired
    private JugadorServicio jugadorServicio;
    @Autowired
    private CeldaServicio celdaServicio;

    private Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/list")
    @Secured({ Role.Code.ADMINISTRADOR, Role.Code.JUGADOR })
    @Operation(summary = "Listar todos los barcos", description = "Obtiene una lista completa de todos los barcos registrados en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de barcos obtenida exitosamente")
    public List<BarcoDTO> listarBarcos() {
        return barcoServicio.listarBarcos();
    }

    @GetMapping("/list/{page}")
    @Operation(summary = "Listar barcos con paginación", description = "Obtiene una lista de barcos paginada (10 por pagina). El número de página debe ser mayor o igual 0.")
     @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista paginada de barcos obtenida exitosamente"),
        @ApiResponse(responseCode = "400", description = "Número de página inválido (debe ser mayor o igual 0)")
    })
    public ResponseEntity<?> listarBarcos(
        @Parameter(description = "Número de página (debe ser mayor a 0)", example = "1", required = true)
        @PathVariable Integer page)
{
        if (page >= 0){
            return ResponseEntity.ok(barcoServicio.listarBarcos(PageRequest.of(page, 10)));    
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO("El numero de pagina debe ser mayor o igual 0"));
        }
        
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar barcos por nombre", description = "Busca barcos que coincidan con el texto proporcionado en el nombre.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Texto de búsqueda vacío o inválido")
    })
    public ResponseEntity<?> buscarBarcos(
        @Parameter(description = "Texto a buscar en el nombre del barco", example = "Velero", required = true)
        @RequestParam(required = false) String searchText) {
        if (searchText != null && searchText.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDTO("El texto de búsqueda no puede estar vacío"));
        }
        
        if (searchText == null || searchText.trim().equals("")) {
            return ResponseEntity.ok(barcoServicio.listarBarcos());
        } else {
            return ResponseEntity.ok(barcoServicio.buscarBarcosPorNombre(searchText.trim()));
        }
    }

    @GetMapping("{id}")
    @Secured({ Role.Code.ADMINISTRADOR, Role.Code.JUGADOR })
    @Operation(summary = "Buscar barco por ID", description = "Obtiene los detalles de un barco específico mediante su identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Barco encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Barco no encontrado")
    })
    public BarcoDTO buscarBarco(
        @Parameter(description = "Identificador único del barco", example = "1", required = true)
        @PathVariable("id") Long id){
        return barcoServicio.buscarBarco(id).orElseThrow();
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo barco", description = "Registra un nuevo barco en el sistema con la información proporcionada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Barco creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public BarcoDTO crearBarco(
        @Parameter(description = "Datos del barco a crear", required = true)
        @RequestBody BarcoDTO barcoDTO) {
        return barcoServicio.crearBarco(barcoDTO);
    }

    @PutMapping
    @Operation(summary = "Actualizar un barco existente", description = "Actualiza la información de un barco existente en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Barco actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Barco no encontrado")
    })
    public BarcoDTO actualizarBarco(
        @Parameter(description = "Datos actualizados del barco (debe incluir el ID)", required = true)
        @RequestBody BarcoDTO barcoDTO) {
        return barcoServicio.actualizarBarco(barcoDTO);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Eliminar un barco", description = "Elimina permanentemente un barco del sistema mediante su identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Barco eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Barco no encontrado")
    })
    public void borrarBarco(
        @Parameter(description = "Identificador único del barco a eliminar", example = "1", required = true)
        @PathVariable long id) {
        barcoServicio.borrarBarco(id);
    }
}
