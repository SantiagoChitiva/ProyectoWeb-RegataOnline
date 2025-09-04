package co.edu.javeriana.proyectoWeb.RegataOnline.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.qos.logback.core.model.Model;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoCeldaDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoJugadorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoModeloDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.mapper.BarcoMapper;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Barco;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Celda;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Jugador;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Modelo;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.BarcoRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.CeldaRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.JugadorRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.ModeloRepositorio;

@Service
public class BarcoServicio {
    @Autowired
    private BarcoRepositorio barcoRepositorio;
    @Autowired
    private JugadorRepositorio jugadorRepositorio;
    @Autowired
    private ModeloRepositorio modeloRepositorio;
    @Autowired
    private CeldaRepositorio celdaRepositorio;
    

    public List<BarcoDTO> listarBarcos() {
        return barcoRepositorio.findAll().stream().map(BarcoMapper::toDTO).toList();
    }

    public Optional<BarcoDTO> buscarBarco(Long id) {
        return barcoRepositorio.findById(id).map(BarcoMapper::toDTO);
    }
    
    public void guardarBarco(BarcoDTO barcoDTO) {
        Barco barco = BarcoMapper.toEntity(barcoDTO);

        if (barcoDTO.getModeloId() != null) {
            Modelo modelo = modeloRepositorio.findById(barcoDTO.getModeloId()).orElse(null);
            barco.setModelo(modelo);
        }
        
        if (barcoDTO.getJugadorId() != null) {
            Jugador jugador = jugadorRepositorio.findById(barcoDTO.getJugadorId()).orElse(null);
            barco.setJugador(jugador);
        }
        if (barcoDTO.getCeldaId() != null) {
            Celda celda = celdaRepositorio.findById(barcoDTO.getCeldaId()).orElse(null);
            barco.setCelda(celda);
            barco.setPosicionX(celda.getPosicionX());
            barco.setPosicionY(celda.getPosicionY());
        }else{
            barco.setCelda(null);
        }


        barcoRepositorio.save(barco);
    }

    public void borrarBarco(long id) {
        barcoRepositorio.deleteById(id);
    }

    public Optional <BarcoJugadorDTO> getBarcoJugador(Long jugadorId){
        Optional<Jugador> jugadorOpt = jugadorRepositorio.findById(jugadorId);

        if(jugadorOpt.isEmpty()){
            return Optional.empty();
        }

        Jugador jugador = jugadorOpt.get();
        List<Long> barcoIds = jugador.getBarcos().stream().map(Barco::getId).toList();

        BarcoJugadorDTO barcoJugadorDTO = new BarcoJugadorDTO(jugadorId, barcoIds);

        return Optional.of(barcoJugadorDTO);
    }

    public List<BarcoDTO> obtenerBarcosPorJugador(Long jugadorId) {
        Optional<Jugador> jugadorOpt = jugadorRepositorio.findById(jugadorId);
        
        if (jugadorOpt.isEmpty()) {
            return List.of();
        }
        
        Jugador jugador = jugadorOpt.get();
        return jugador.getBarcos().stream().map(BarcoMapper::toDTO).toList();
    }

    public void updateBarcosJugador(BarcoJugadorDTO barcoJugadorDTO){
        Jugador jugador = jugadorRepositorio.findById(barcoJugadorDTO.getJugadorId()).orElseThrow();
        
        // Limpiar los barcos actuales del jugador
        jugador.getBarcos().clear();
        
        // Solo buscar barcos si la lista de IDs no es null y no está vacía
        if (barcoJugadorDTO.getBarcosIds() != null && !barcoJugadorDTO.getBarcosIds().isEmpty()) {
            List<Barco> barcosSeleccionados = barcoRepositorio.findAllById(barcoJugadorDTO.getBarcosIds());
            
            // Actualizar la relación bidireccional
            for (Barco barco : barcosSeleccionados) {
                barco.setJugador(jugador);
            }
            
            jugador.getBarcos().addAll(barcosSeleccionados);
        }
        
        jugadorRepositorio.save(jugador);
    }

    public void actualizarModeloDeBarcos(BarcoModeloDTO barcoModeloDTO) {
        Modelo nuevoModelo = null;
        
        // Obtener el nuevo modelo si se proporciona
        if (barcoModeloDTO.getModeloId() != null) {
            nuevoModelo = modeloRepositorio.findById(barcoModeloDTO.getModeloId()).orElseThrow();
        }
        
        // Procesar cada barco
        if (barcoModeloDTO.getBarcosIds() != null && !barcoModeloDTO.getBarcosIds().isEmpty()) {
            List<Barco> barcosAActualizar = barcoRepositorio.findAllById(barcoModeloDTO.getBarcosIds());
            
            for (Barco barco : barcosAActualizar) {
                // Remover barco del modelo anterior (si tenía uno)
                if (barco.getModelo() != null) {
                    barco.getModelo().getBarcos().remove(barco);
                }
                
                // Asignar nuevo modelo
                if (nuevoModelo != null) {
                    barco.setModelo(nuevoModelo);
                    nuevoModelo.getBarcos().add(barco);
                } else {
                    barco.setModelo(null);
                }
            }
            
            // Guardar todos los cambios
            barcoRepositorio.saveAll(barcosAActualizar);
            if (nuevoModelo != null) {
                modeloRepositorio.save(nuevoModelo);
            }
        }
    }

    public Optional <BarcoModeloDTO> getBarcoModelo(Long barcoId){
        Optional<Modelo> modeloBarcoOpt = modeloRepositorio.findById(barcoId);

        if(modeloBarcoOpt.isEmpty()){
            return Optional.empty();
        }

        Modelo modeloBarco = modeloBarcoOpt.get();
        List<Long> modelosIds = modeloBarco.getBarcos().stream().map(Barco::getId).toList();

        BarcoModeloDTO barcoModeloDTO = new BarcoModeloDTO(barcoId, modelosIds);

        return Optional.of(barcoModeloDTO);
    }

    public Optional<BarcoCeldaDTO> getBarcoCelda(Long celdaId) {
        Optional<Celda> celdaOpt = celdaRepositorio.findById(celdaId);

        if (celdaOpt.isEmpty()) {
            return Optional.empty();
        }

        Celda celda = celdaOpt.get();
        List<Long> barcoIds = celda.getBarcos().stream().map(Barco::getId).toList();

        BarcoCeldaDTO barcoCeldaDTO = new BarcoCeldaDTO(celdaId, barcoIds);

        return Optional.of(barcoCeldaDTO);
    }

    public void actualizarCeldaDeBarcos(BarcoCeldaDTO barcoCeldaDTO) {
        Celda nuevaCelda = null;
        
        // Obtener la nueva celda si se proporciona
        if (barcoCeldaDTO.getCeldaId() != null) {
            nuevaCelda = celdaRepositorio.findById(barcoCeldaDTO.getCeldaId()).orElseThrow();
        }
        
        // Procesar cada barco
        if (barcoCeldaDTO.getBarcosIds() != null && !barcoCeldaDTO.getBarcosIds().isEmpty()) {
            List<Barco> barcosAActualizar = barcoRepositorio.findAllById(barcoCeldaDTO.getBarcosIds());
            
            for (Barco barco : barcosAActualizar) {
                // Remover barco de la celda anterior (si tenía una)
                if (barco.getCelda() != null) {
                    barco.getCelda().getBarcos().remove(barco);
                }
                
                // Asignar nueva celda
                if (nuevaCelda != null) {
                    barco.setCelda(nuevaCelda);
                    nuevaCelda.getBarcos().add(barco);
                } else {
                    barco.setCelda(null);
                }
            }
            
            // Guardar todos los cambios
            barcoRepositorio.saveAll(barcosAActualizar);
            if (nuevaCelda != null) {
                celdaRepositorio.save(nuevaCelda);
            }
        }
    }

    public List<BarcoDTO> obtenerBarcosPorCelda(Long celdaId) {
        Optional<Celda> celdaOpt = celdaRepositorio.findById(celdaId);
        
        if (celdaOpt.isEmpty()) {
            return List.of();
        }
        
        Celda celda = celdaOpt.get();
        return celda.getBarcos().stream().map(BarcoMapper::toDTO).toList();
    }
}
