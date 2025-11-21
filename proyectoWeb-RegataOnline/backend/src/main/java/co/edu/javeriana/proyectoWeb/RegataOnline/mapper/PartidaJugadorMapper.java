package co.edu.javeriana.proyectoWeb.RegataOnline.mapper;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.PartidaJugadorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.PartidaJugador;

public class PartidaJugadorMapper {
    
    public static PartidaJugadorDTO toDTO(PartidaJugador pj) {
        if (pj == null) {
            return null;
        }
        
        PartidaJugadorDTO dto = new PartidaJugadorDTO();
        dto.setId(pj.getId());
        dto.setPartidaId(pj.getPartida().getId());
        dto.setJugadorId(pj.getJugador().getId());
        dto.setJugadorNombre(pj.getJugador().getNombre());
        dto.setBarcoId(pj.getBarco().getId());
        dto.setBarcoNombre(pj.getBarco().getNombre());
        
        if (pj.getBarco().getModelo() != null) {
            dto.setBarcoColor(pj.getBarco().getModelo().getColor());
        }
        
        dto.setOrdenTurno(pj.getOrdenTurno());
        dto.setPosicionX(pj.getPosicionX());
        dto.setPosicionY(pj.getPosicionY());
        dto.setVelocidadX(pj.getVelocidadX());
        dto.setVelocidadY(pj.getVelocidadY());
        dto.setEstado(pj.getEstado());
        dto.setHaLlegadoMeta(pj.getHaLlegadoMeta());
        dto.setPosicionFinal(pj.getPosicionFinal());
        dto.setMovimientosRealizados(pj.getMovimientosRealizados());
        
        return dto;
    }
}
