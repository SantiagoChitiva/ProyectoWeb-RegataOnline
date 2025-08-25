package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import co.edu.javeriana.proyectoWeb.RegataOnline.model.Jugador;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.JugadorServicio;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/jugador")
public class JugadorControlador {
    @Autowired
    private JugadorServicio jugadorServicio;

    private Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/list")
    public ModelAndView listarJugadores() {
        log.info("Lista de Jugadores");
        List<Jugador> jugadores = jugadorServicio.listarJugadores();
        ModelAndView modelAndView = new ModelAndView("jugador-lista");
        modelAndView.addObject("listaJugadores", jugadores);
        return modelAndView;
    }

    @GetMapping("/view/{idJugador}")   
    public ModelAndView buscarJugador(@PathVariable("idJugador") Long id){
        Jugador jugador = jugadorServicio.buscarJugador(id);
        ModelAndView modelAndView = new ModelAndView("jugador-view");
        modelAndView.addObject("jugador", jugador);
        return modelAndView;
    }
    
    
}
