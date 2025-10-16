
package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoJugadorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.BarcoServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/jugador/barcos")
@Tag(name = "BarcoJugador", description = "Endpoints para gestionar la relación entre jugador y barcos")
public class BarcoJugadorControlador {
    @Autowired
    private BarcoServicio barcoServicio;

    @GetMapping("/{jugadorId}")
    @Operation(summary = "Obtener barcos de un jugador", description = "Devuelve los barcos asociados a un jugador específico")
    @ApiResponse(responseCode = "200", description = "Barcos del jugador obtenidos exitosamente")
    public Optional<BarcoJugadorDTO> getBarcoJugador(
        @Parameter(description = "ID del jugador", example = "1", required = true)
        @PathVariable Long jugadorId) {
        return barcoServicio.getBarcoJugador(jugadorId);
    }

    @PostMapping("/save")
    @Operation(summary = "Actualizar barcos de un jugador", description = "Actualiza la relación de barcos para un jugador")
    @ApiResponse(responseCode = "200", description = "Barcos del jugador actualizados exitosamente")
    public void saveBarcosJugador(
        @Parameter(description = "Datos de la relación jugador-barcos", required = true)
        @RequestBody BarcoJugadorDTO barcoJugadorDTO) {
        barcoServicio.updateBarcosJugador(barcoJugadorDTO);
    }
}
