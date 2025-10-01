package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.ErrorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.ModeloDTO;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/modelo")   
@Tag(name = "Modelo", description = "Endpoints para gestionar los modelos de barcos") 
public class ModeloControlador {
    @Autowired 
    private ModeloServicio modeloServicio;

    private Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/list")
    @Operation(summary = "Listar todos los modelos", description = "Obtiene una lista completa de todos los modelos de barcos registrados en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de modelos obtenida exitosamente")
    public List<ModeloDTO> listarModelos() {
        return modeloServicio.listarModelos();
    }

    @GetMapping("/list/{page}")
    @Operation(summary = "Listar modelos con paginación", description = "Obtiene una lista de modelos paginada (5 por página). El número de página debe ser mayor o igual a 0.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista paginada de modelos obtenida exitosamente"),
        @ApiResponse(responseCode = "400", description = "Número de página inválido (debe ser mayor o igual a 0)")
    })
    public ResponseEntity<?> listarModelos(
        @Parameter(description = "Número de página (debe ser mayor o igual a 0)", example = "0", required = true)
        @PathVariable Integer page) {
        if (page >= 0) {
            return ResponseEntity.ok(modeloServicio.listarModelos(PageRequest.of(page, 5)));    
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDTO("El numero de pagina debe ser mayor o igual a 0"));
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar modelos por nombre", description = "Busca modelos que coincidan con el texto proporcionado en el nombre.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Texto de búsqueda vacío o inválido")
    })
    public ResponseEntity<?> buscarModelos(
        @Parameter(description = "Texto a buscar en el nombre del modelo", example = "Velero", required = true)
        @RequestParam(required = false) String searchText) {
        if (searchText != null && searchText.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDTO("El texto de búsqueda no puede estar vacío"));
        }
        
        if (searchText == null || searchText.trim().equals("")) {
            return ResponseEntity.ok(modeloServicio.listarModelos());
        } else {
            return ResponseEntity.ok(modeloServicio.buscarModelosPorNombre(searchText.trim()));
        }
    }

    @GetMapping("{id}")
    @Operation(summary = "Buscar modelo por ID", description = "Obtiene los detalles de un modelo específico mediante su identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Modelo encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Modelo no encontrado")
    })
    public ModeloDTO buscarModelo(
        @Parameter(description = "Identificador único del modelo", example = "1", required = true)
        @PathVariable("id") Long id) {
        return modeloServicio.buscarModelo(id).orElseThrow();
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo modelo", description = "Registra un nuevo modelo de barco en el sistema con la información proporcionada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Modelo creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ModeloDTO crearModelo(
        @Parameter(description = "Datos del modelo a crear", required = true)
        @RequestBody ModeloDTO modeloDTO) {
        return modeloServicio.guardarModelo(modeloDTO);
    }

    @PutMapping
    @Operation(summary = "Actualizar un modelo existente", description = "Actualiza la información de un modelo existente en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Modelo actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Modelo no encontrado")
    })
    public ModeloDTO actualizarModelo(
        @Parameter(description = "Datos actualizados del modelo (debe incluir el ID)", required = true)
        @RequestBody ModeloDTO modeloDTO) {
        return modeloServicio.guardarModelo(modeloDTO);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Eliminar un modelo", description = "Elimina permanentemente un modelo del sistema mediante su identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Modelo eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Modelo no encontrado")
    })
    public void borrarModelo(
        @Parameter(description = "Identificador único del modelo a eliminar", example = "1", required = true)
        @PathVariable long id) {
        modeloServicio.borrarModelo(id);
    }
}