package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoModeloDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Role;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.BarcoServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/barco/modelos")
@Secured({ Role.Code.ADMINISTRADOR })
@Tag(name = "Barco-Modelo", description = "Endpoints para gestionar la relación entre barcos y modelos. Solo ADMINISTRADOR.") 
public class BarcoModeloControlador {
    @Autowired
    private BarcoServicio barcoServicio;

    private Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/{barcoId}")
    @Operation(summary = "Obtener relación barco-modelo", description = "Obtiene la información de la relación entre un barco específico y su modelo asignado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Relación barco-modelo obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Barco no encontrado")
    })
    public BarcoModeloDTO obtenerBarcoModelo(
        @Parameter(description = "Identificador único del barco", example = "1", required = true)
        @PathVariable Long barcoId) {
        return barcoServicio.getBarcoModelo(barcoId).orElseThrow();
    }

    @PutMapping
    @Operation(summary = "Actualizar modelo del barco", description = "Actualiza el modelo asignado a un barco específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Modelo del barco actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Barco o modelo no encontrado")
    })
    public BarcoModeloDTO actualizarBarcoModelo(
        @Parameter(description = "Datos de la relación barco-modelo a actualizar", required = true)
        @RequestBody BarcoModeloDTO barcoModeloDTO) {
        return barcoServicio.actualizarModeloDeBarcos(barcoModeloDTO);
    }
}