package co.edu.javeriana.proyectoWeb.RegataOnline.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.JugadorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.mapper.BarcoMapper;
import co.edu.javeriana.proyectoWeb.RegataOnline.mapper.JugadorMapper;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Barco;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Jugador;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.JugadorRepositorio;

@Service
public class JugadorServicio {
    @Autowired
    private JugadorRepositorio jugadorRepositorio;

    public List<JugadorDTO> listarJugadores(){
        return jugadorRepositorio.findAll().stream().map(JugadorMapper::toDTO).toList();
    }

    public List<JugadorDTO> listarJugadores(Pageable pageable){
        return jugadorRepositorio.findAll(pageable).stream().map(JugadorMapper::toDTO).toList();
    }

    public Optional<JugadorDTO> buscarJugador(Long id){
        return jugadorRepositorio.findById(id).map(JugadorMapper::toDTO);
    }
    
    public void guardarJugador(JugadorDTO jugadorDTO){
        Jugador jugador = JugadorMapper.toEntity(jugadorDTO);
        jugadorRepositorio.save(jugador);
    }

    public void borrarJugador(long id) {
        Optional<Jugador> jugadorOpt = jugadorRepositorio.findById(id);
        if (jugadorOpt.isPresent()) {
            Jugador jugador = jugadorOpt.get();

            for (Barco barco : jugador.getBarcos()) {
            barco.setJugador(null);
            }

            jugador.getBarcos().clear();

            jugadorRepositorio.save(jugador);

            jugadorRepositorio.deleteById(id);
        }
    }

    public List<JugadorDTO> buscarJugadoresPorNombre(String searchText) {
        return jugadorRepositorio.findByNombreContainingIgnoreCase(searchText).stream().map(JugadorMapper::toDTO).toList();
    }
}
