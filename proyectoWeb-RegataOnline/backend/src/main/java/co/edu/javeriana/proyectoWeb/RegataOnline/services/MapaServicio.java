package co.edu.javeriana.proyectoWeb.RegataOnline.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.CeldaDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.CrearMapaRequest;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.MapaDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.mapper.MapaMapper;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Celda;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Mapa;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.CeldaRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.MapaRepositorio;

@Service
public class MapaServicio {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MapaRepositorio mapaRepositorio;

    @Autowired
    private CeldaRepositorio celdaRepositorio;

    @Transactional
    public MapaDTO crearMapa(CrearMapaRequest request) {
        log.info("Creando mapa de {}x{}", request.getFilas(), request.getColumnas());
        
        // Crear el mapa
        Mapa mapa = new Mapa(request.getFilas(), request.getColumnas());
        mapa = mapaRepositorio.save(mapa);
        
        // Crear todas las celdas del mapa
        List<Celda> celdas = new ArrayList<>();
        
        for (int i = 0; i < request.getFilas(); i++) {
            for (int j = 0; j < request.getColumnas(); j++) {
                Celda celda = new Celda();
                celda.setPosicionX(i);
                celda.setPosicionY(j);
                celda.setTipo(""); // Por defecto, agua (vacío)
                celda.setMapa(mapa);
                celdas.add(celda);
            }
        }
        
        // Aplicar las celdas seleccionadas (con tipos especiales)
        if (request.getCeldasSeleccionadas() != null) {
            for (CeldaDTO celdaConfig : request.getCeldasSeleccionadas()) {
                int index = celdaConfig.getPosicionX() * request.getColumnas() + celdaConfig.getPosicionY();
                if (index >= 0 && index < celdas.size()) {
                    celdas.get(index).setTipo(celdaConfig.getTipo());
                }
            }
        }
        
        // Guardar todas las celdas
        celdaRepositorio.saveAll(celdas);
        mapa.setCeldas(celdas);
        
        log.info("Mapa creado exitosamente con ID: {}", mapa.getId());
        return MapaMapper.toDTO(mapa);
    }

    public List<MapaDTO> listarMapas() {
        log.info("Listando todos los mapas");
        return mapaRepositorio.findAll().stream()
            .map(MapaMapper::toDTO)
            .toList();
    }

    public Optional<MapaDTO> buscarMapa(Long id) {
        return mapaRepositorio.findById(id)
            .map(MapaMapper::toDTO);
    }

    @Transactional
    public void borrarMapa(Long id) {
        log.info("Eliminando mapa con ID: {}", id);
        
        Mapa mapa = mapaRepositorio.findById(id)
            .orElseThrow(() -> new RuntimeException("Mapa no encontrado con ID: " + id));
        
        // Obtener las celdas del mapa
        List<Celda> celdas = celdaRepositorio.findByMapa(mapa);
        log.info("Mapa {} tiene {} celdas", id, celdas.size());
        
        // Verificar si hay barcos en alguna celda
        boolean tieneBarcos = false;
        for (Celda celda : celdas) {
            if (celda.getBarcos() != null && !celda.getBarcos().isEmpty()) {
                log.warn("La celda {} tiene {} barcos asociados", celda.getId(), celda.getBarcos().size());
                tieneBarcos = true;
                break;
            }
        }
        
        if (tieneBarcos) {
            throw new RuntimeException("No se puede eliminar el mapa porque tiene barcos asociados. Elimine los barcos primero.");
        }
        
        // Primero desasociar las celdas del mapa para evitar problemas de cascade
        for (Celda celda : celdas) {
            celda.setMapa(null);
        }
        celdaRepositorio.saveAll(celdas);
        
        // Eliminar las celdas
        celdaRepositorio.deleteAll(celdas);
        log.info("Eliminadas {} celdas del mapa {}", celdas.size(), id);
        
        // Finalmente eliminar el mapa
        mapaRepositorio.delete(mapa);
        log.info("Mapa {} eliminado exitosamente", id);
    }
}
