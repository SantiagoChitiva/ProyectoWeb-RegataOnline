package co.edu.javeriana.proyectoWeb.RegataOnline.mapper;

import java.util.stream.Collectors;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.MapaDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Mapa;

public class MapaMapper {
    public static MapaDTO toDTO(Mapa mapa) {
        MapaDTO mapaDTO = new MapaDTO();
        mapaDTO.setId(mapa.getId());
        mapaDTO.setFilas(mapa.getFilas());
        mapaDTO.setColumnas(mapa.getColumnas());
        
        if (mapa.getCeldas() != null) {
            mapaDTO.setCeldas(
                mapa.getCeldas().stream()
                    .map(CeldaMapper::toDTO)
                    .collect(Collectors.toList())
            );
        }
        
        return mapaDTO;
    }

    public static Mapa toEntity(MapaDTO mapaDTO) {
        Mapa mapa = new Mapa();
        mapa.setId(mapaDTO.getId());
        mapa.setFilas(mapaDTO.getFilas());
        mapa.setColumnas(mapaDTO.getColumnas());
        
        if (mapaDTO.getCeldas() != null) {
            mapa.setCeldas(
                mapaDTO.getCeldas().stream()
                    .map(CeldaMapper::toEntity)
                    .collect(Collectors.toList())
            );
        }
        
        return mapa;
    }
}
