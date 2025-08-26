package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.JugadorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Jugador;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.JugadorServicio;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


@Controller
@RequestMapping("/jugador")
public class JugadorControlador {
    @Autowired
    private JugadorServicio jugadorServicio;

    private Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/list")
    public ModelAndView listarJugadores() {
        log.info("Lista de Jugadores");
        List<JugadorDTO> jugadores = jugadorServicio.listarJugadores();
        ModelAndView modelAndView = new ModelAndView("jugador-lista");
        modelAndView.addObject("listaJugadores", jugadores);
        return modelAndView;
    }

    @GetMapping("/view/{idJugador}")   
    public ModelAndView buscarJugador(@PathVariable("idJugador") Long id){
        JugadorDTO jugador = jugadorServicio.buscarJugador(id).orElseThrow();
        ModelAndView modelAndView = new ModelAndView("jugador-view");
        modelAndView.addObject("jugador", jugador);
        return modelAndView;
    }

    @GetMapping("/create")   
    public ModelAndView formularioCrearJugador(){
        ModelAndView modelAndView = new ModelAndView("jugador-edit");
        modelAndView.addObject("jugador", new JugadorDTO());
        return modelAndView;
    }

    @GetMapping("/edit/{id}")   
    public ModelAndView formularioEditarJugador(@PathVariable Long id){
        JugadorDTO jugadorDTO = jugadorServicio.buscarJugador(id).orElseThrow();
        ModelAndView modelAndView = new ModelAndView("jugador-edit");
        modelAndView.addObject("jugador", jugadorDTO);
        return modelAndView;
    }

    @PostMapping("/save")
    public RedirectView guardarJugador(@ModelAttribute JugadorDTO jugadorDTO){
        jugadorServicio.guardarJugador(jugadorDTO);
        return new RedirectView("/jugador/list");
    }

    @GetMapping("/delete/{id}")
    public RedirectView borrarJugador(@PathVariable long id){
        jugadorServicio.borrarJugador(id);
        return new RedirectView("/jugador/list");
    }


    
    
}
