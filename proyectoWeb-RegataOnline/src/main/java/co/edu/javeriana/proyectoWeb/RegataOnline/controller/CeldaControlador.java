
package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.CeldaDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.CeldaServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/celda")
@Tag(name = "Celda", description = "Endpoints para gestionar las celdas")
public class CeldaControlador {
    @Autowired
    private CeldaServicio celdaServicio;

    @GetMapping("/list")
    @Operation(summary = "Listar todas las celdas", description = "Obtiene una lista de todas las celdas")
    @ApiResponse(responseCode = "200", description = "Lista de celdas obtenida exitosamente")
    public List<CeldaDTO> listarCeldas() {
        return celdaServicio.listarCeldas();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar celda por ID", description = "Obtiene los detalles de una celda específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Celda encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Celda no encontrada")
    })
    public Optional<CeldaDTO> buscarCelda(
        @Parameter(description = "ID de la celda", example = "1", required = true)
        @PathVariable("id") Long id) {
        return celdaServicio.buscarCelda(id);
    }
}