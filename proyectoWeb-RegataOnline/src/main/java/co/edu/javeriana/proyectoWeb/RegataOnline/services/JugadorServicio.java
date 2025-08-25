package co.edu.javeriana.proyectoWeb.RegataOnline.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.javeriana.proyectoWeb.RegataOnline.model.Jugador;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.JugadorRepositorio;

@Service
public class JugadorServicio {
    @Autowired
    private JugadorRepositorio jugadorRepositorio;

    public List<Jugador> listarJugadores(){
        return jugadorRepositorio.findAll();
    }

    public Jugador buscarJugador(Long id){
        return jugadorRepositorio.findById(id).orElseThrow();
    }
    
}
