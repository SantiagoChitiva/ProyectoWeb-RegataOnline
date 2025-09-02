package co.edu.javeriana.proyectoWeb.RegataOnline.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoJugadorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoModeloDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.mapper.BarcoMapper;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Barco;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Jugador;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Modelo;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.BarcoRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.JugadorRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.ModeloRepositorio;

@Service
public class BarcoServicio {
    @Autowired
    private BarcoRepositorio barcoRepositorio;
    @Autowired
    private JugadorRepositorio jugadorRepositorio;
    @Autowired
    private ModeloRepositorio modeloBarcoRepositorio;

    public List<BarcoDTO> listarBarcos() {
        return barcoRepositorio.findAll().stream().map(BarcoMapper::toDTO).toList();
    }

    public Optional<BarcoDTO> buscarBarco(Long id) {
        return barcoRepositorio.findById(id).map(BarcoMapper::toDTO);
    }
    
    public void guardarBarco(BarcoDTO barcoDTO) {
        Barco barco = BarcoMapper.toEntity(barcoDTO);
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

    public Optional <BarcoModeloDTO> getBarcoModelo(Long barcoId){
        Optional<Modelo> modeloBarcoOpt = modeloBarcoRepositorio.findById(barcoId);

        if(modeloBarcoOpt.isEmpty()){
            return Optional.empty();
        }

        Modelo modeloBarco = modeloBarcoOpt.get();
        List<Long> modelosIds = modeloBarco.getBarcos().stream().map(Barco::getId).toList();

        BarcoModeloDTO barcoModeloDTO = new BarcoModeloDTO(barcoId, modelosIds);

        return Optional.of(barcoModeloDTO);
    }
}
