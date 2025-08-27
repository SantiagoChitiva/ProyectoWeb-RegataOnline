package co.edu.javeriana.proyectoWeb.RegataOnline.mapper;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Barco;

public class BarcoMapper {
    public static BarcoDTO toDTO(Barco barco){
        BarcoDTO barcoDTO = new BarcoDTO();
        barcoDTO.setId(barco.getId());
        barcoDTO.setVelocidadX(barco.getVelocidadX());
        barcoDTO.setVelocidadY(barco.getVelocidadY());
        barcoDTO.setPosicionX(barco.getPosicionX());
        barcoDTO.setPosicionY(barco.getPosicionY());
        if(barco.getModelo() != null){
            barcoDTO.setModeloId(barco.getModelo().getId());
            barcoDTO.setNombreModeloBarco(barco.getModelo().getNombreModelo());
        }
        if(barco.getJugador() != null){
            barcoDTO.setJugadorId(barco.getJugador().getId());
            barcoDTO.setNombreJugador(barco.getJugador().getNombre());
        }
        if(barco.getCelda() != null){
            barcoDTO.setCeldaId(barco.getCelda().getId());
        }
        return barcoDTO;
    }

    public static Barco toEntity(BarcoDTO barcoDTO){
        Barco barco = new Barco();
        barco.setId(barcoDTO.getId());
        barco.setVelocidadX(barcoDTO.getVelocidadX());
        barco.setVelocidadY(barcoDTO.getVelocidadY());
        barco.setPosicionX(barcoDTO.getPosicionX());
        barco.setPosicionY(barcoDTO.getPosicionY());
        return barco;

    }
    
}
