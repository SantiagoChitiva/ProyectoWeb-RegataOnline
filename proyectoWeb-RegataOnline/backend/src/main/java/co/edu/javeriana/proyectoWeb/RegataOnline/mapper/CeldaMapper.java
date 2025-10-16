package co.edu.javeriana.proyectoWeb.RegataOnline.mapper;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.CeldaDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Celda;

public class CeldaMapper {
    public static CeldaDTO toDTO(Celda celda) {
        CeldaDTO celdaDTO = new CeldaDTO();
        celdaDTO.setId(celda.getId());
        celdaDTO.setTipo(celda.getTipo());
        celdaDTO.setPosicionX(celda.getPosicionX());
        celdaDTO.setPosicionY(celda.getPosicionY());
        return celdaDTO;
    }

    public static Celda toEntity(CeldaDTO celdaDTO) {
        Celda celda = new Celda();
        celda.setId(celdaDTO.getId());
        celda.setTipo(celdaDTO.getTipo());
        celda.setPosicionX(celdaDTO.getPosicionX());
        celda.setPosicionY(celdaDTO.getPosicionY());
        return celda;
    }
}