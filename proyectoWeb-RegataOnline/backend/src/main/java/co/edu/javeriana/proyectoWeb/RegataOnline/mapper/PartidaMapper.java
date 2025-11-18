package co.edu.javeriana.proyectoWeb.RegataOnline.mapper;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.PartidaDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Partida;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.PartidaJugador;

/**
 * Mapper para partidas de un solo jugador (modo clásico)
 * Adaptado para trabajar con la nueva estructura multijugador
 */
public class PartidaMapper {
    
    public static PartidaDTO toDTO(Partida partida) {
        PartidaDTO dto = new PartidaDTO();
        dto.setId(partida.getId());
        dto.setEstado(partida.getEstado());
        dto.setFechaInicio(partida.getFechaInicio());
        dto.setFechaUltimaJugada(partida.getFechaUltimaJugada());
        
        // Para compatibilidad con single player, obtener datos del primer PartidaJugador
        if (partida.getJugadores() != null && !partida.getJugadores().isEmpty()) {
            PartidaJugador pj = partida.getJugadores().get(0);
            
            // Datos del jugador
            if (pj.getJugador() != null) {
                dto.setJugadorId(pj.getJugador().getId());
                dto.setJugadorNombre(pj.getJugador().getNombre());
            }
            
            // Datos del barco
            if (pj.getBarco() != null) {
                dto.setBarcoId(pj.getBarco().getId());
                dto.setBarcoNombre(pj.getBarco().getNombre());
                dto.setBarcoPosicionX(pj.getPosicionX());
                dto.setBarcoPosicionY(pj.getPosicionY());
                dto.setBarcoVelocidadX(pj.getVelocidadX());
                dto.setBarcoVelocidadY(pj.getVelocidadY());
            }
            
            // Estado del juego
            dto.setMovimientos(pj.getMovimientosRealizados());
            dto.setHaLlegadoMeta(pj.getHaLlegadoMeta());
        }
        
        // Datos del mapa
        if (partida.getMapa() != null) {
            dto.setMapaId(partida.getMapa().getId());
            dto.setMapaFilas(partida.getMapa().getFilas());
            dto.setMapaColumnas(partida.getMapa().getColumnas());
        }
        
        return dto;
    }
}
