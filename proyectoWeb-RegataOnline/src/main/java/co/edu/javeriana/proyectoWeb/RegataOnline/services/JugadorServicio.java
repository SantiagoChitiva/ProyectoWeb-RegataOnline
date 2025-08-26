package co.edu.javeriana.proyectoWeb.RegataOnline.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.JugadorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.mapper.JugadorMapper;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Jugador;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.JugadorRepositorio;

@Service
public class JugadorServicio {
    @Autowired
    private JugadorRepositorio jugadorRepositorio;

    public List<JugadorDTO> listarJugadores(){
        return jugadorRepositorio.findAll().stream().map(JugadorMapper::toDTO).toList();

    }

    public Optional<JugadorDTO> buscarJugador(Long id){
        return jugadorRepositorio.findById(id).map(JugadorMapper::toDTO);
    }
    
    public void guardarJugador(JugadorDTO jugadorDTO){
        Jugador jugador = JugadorMapper.toEntity(jugadorDTO);
        jugadorRepositorio.save(jugador);
    }

    public void borrarJugador(long id) {
        jugadorRepositorio.deleteById(id);
    }


}
