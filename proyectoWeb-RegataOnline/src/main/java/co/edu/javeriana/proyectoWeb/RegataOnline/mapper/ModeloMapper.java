package co.edu.javeriana.proyectoWeb.RegataOnline.mapper;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.ModeloDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Modelo;

public class ModeloMapper {
    public static ModeloDTO toDTO(Modelo modelo){
        ModeloDTO modeloDTO = new ModeloDTO();
        modeloDTO.setId(modelo.getId());
        modeloDTO.setNombreModelo(modelo.getNombreModelo());
        modeloDTO.setColor(modelo.getColor());
        return modeloDTO;
    }

    public static Modelo toEntity(ModeloDTO modeloDTO){
        Modelo modelo = new Modelo();
        modeloDTO.setId(modelo.getId());
        modeloDTO.setNombreModelo(modelo.getNombreModelo());
        modeloDTO.setColor(modelo.getColor());
        return modelo;

    }

}
