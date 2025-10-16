package co.edu.javeriana.proyectoWeb.RegataOnline.mapper;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.PartidaDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Partida;

public class PartidaMapper {
    
    public static PartidaDTO toDTO(Partida partida) {
        PartidaDTO dto = new PartidaDTO();
        dto.setId(partida.getId());
        dto.setEstado(partida.getEstado());
        dto.setFechaInicio(partida.getFechaInicio());
        dto.setFechaUltimaJugada(partida.getFechaUltimaJugada());
        dto.setMovimientos(partida.getMovimientos());
        dto.setHaLlegadoMeta(partida.getHaLlegadoMeta());
        
        // Datos del jugador
        if (partida.getJugador() != null) {
            dto.setJugadorId(partida.getJugador().getId());
            dto.setJugadorNombre(partida.getJugador().getNombre());
        }
        
        // Datos del mapa
        if (partida.getMapa() != null) {
            dto.setMapaId(partida.getMapa().getId());
            dto.setMapaFilas(partida.getMapa().getFilas());
            dto.setMapaColumnas(partida.getMapa().getColumnas());
        }
        
        // Datos del barco
        if (partida.getBarco() != null) {
            dto.setBarcoId(partida.getBarco().getId());
            dto.setBarcoNombre(partida.getBarco().getNombre());
            dto.setBarcoPosicionX(partida.getBarco().getPosicionX());
            dto.setBarcoPosicionY(partida.getBarco().getPosicionY());
        }
        
        return dto;
    }
}
