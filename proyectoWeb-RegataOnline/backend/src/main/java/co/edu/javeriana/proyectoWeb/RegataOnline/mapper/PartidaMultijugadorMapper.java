package co.edu.javeriana.proyectoWeb.RegataOnline.mapper;

import java.util.List;
import java.util.stream.Collectors;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.PartidaDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.PartidaJugadorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Partida;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.PartidaJugador;

public class PartidaMultijugadorMapper {
    
    public static PartidaDTO toDTO(Partida partida, List<PartidaJugador> jugadores) {
        if (partida == null) {
            return null;
        }
        
        PartidaDTO dto = new PartidaDTO();
        dto.setId(partida.getId());
        dto.setJugadorCreadorId(partida.getJugadorCreador().getId());
        dto.setJugadorCreadorNombre(partida.getJugadorCreador().getNombre());
        dto.setMapaId(partida.getMapa().getId());
        dto.setEstado(partida.getEstado());
        dto.setNumeroTurnoActual(partida.getNumeroTurnoActual());
        dto.setOrdenTurnoActual(partida.getOrdenTurnoActual());
        dto.setCantidadJugadores(partida.getCantidadJugadores());
        dto.setMaxJugadores(partida.getMaxJugadores());
        dto.setFechaInicio(partida.getFechaInicio());
        dto.setFechaUltimaJugada(partida.getFechaUltimaJugada());
        dto.setFechaFin(partida.getFechaFin());
        
        if (partida.getGanador() != null) {
            dto.setGanadorId(partida.getGanador().getId());
            dto.setGanadorNombre(partida.getGanador().getNombre());
        }

        // Convertir jugadores
        List<PartidaJugadorDTO> jugadoresDTO = jugadores.stream()
            .map(PartidaJugadorMapper::toDTO)
            .collect(Collectors.toList());
        dto.setJugadores(jugadoresDTO);

        return dto;
    }
}
