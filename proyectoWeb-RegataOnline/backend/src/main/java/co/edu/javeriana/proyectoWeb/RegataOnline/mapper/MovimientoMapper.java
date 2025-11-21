package co.edu.javeriana.proyectoWeb.RegataOnline.mapper;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.MovimientoDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Movimiento;

public class MovimientoMapper {
    
    public static MovimientoDTO toDTO(Movimiento mov) {
        if (mov == null) {
            return null;
        }
        
        MovimientoDTO dto = new MovimientoDTO();
        dto.setId(mov.getId());
        dto.setPartidaJugadorId(mov.getPartidaJugador().getId());
        dto.setNumeroTurno(mov.getNumeroTurno());
        dto.setAceleracionX(mov.getAceleracionX());
        dto.setAceleracionY(mov.getAceleracionY());
        dto.setVelocidadXAnterior(mov.getVelocidadXAnterior());
        dto.setVelocidadYAnterior(mov.getVelocidadYAnterior());
        dto.setVelocidadXNueva(mov.getVelocidadXNueva());
        dto.setVelocidadYNueva(mov.getVelocidadYNueva());
        dto.setPosicionXAnterior(mov.getPosicionXAnterior());
        dto.setPosicionYAnterior(mov.getPosicionYAnterior());
        dto.setPosicionXNueva(mov.getPosicionXNueva());
        dto.setPosicionYNueva(mov.getPosicionYNueva());
        dto.setLlegoAMeta(mov.getLlegoAMeta());
        dto.setChocoConPared(mov.getChocoConPared());
        
        return dto;
    }
}
