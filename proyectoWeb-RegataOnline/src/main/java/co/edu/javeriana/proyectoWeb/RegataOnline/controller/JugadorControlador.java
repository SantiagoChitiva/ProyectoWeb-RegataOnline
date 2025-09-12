package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import java.util.List;
import java.util.Optional;

import org.hibernate.query.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoJugadorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.JugadorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Barco;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Jugador;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.BarcoServicio;
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

    @Autowired
    private BarcoServicio barcoServicio;

    private Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/list")
    public ModelAndView listarJugadores() {
        log.info("Lista de Jugadores");
        List<JugadorDTO> jugadores = jugadorServicio.listarJugadores();
        ModelAndView modelAndView = new ModelAndView("jugador-lista");
        modelAndView.addObject("listaJugadores", jugadores);
        return modelAndView;
    }

    @GetMapping("/list/{page}")
    public ModelAndView listarJugadores(@PathVariable Integer page) {
        log.info("Lista de Jugadores");
        List<JugadorDTO> jugadores = jugadorServicio.listarJugadores(PageRequest.of(page, 2));
        ModelAndView modelAndView = new ModelAndView("jugador-lista");
        modelAndView.addObject("listaJugadores", jugadores);
        return modelAndView;
    }

    @GetMapping("/search")
    public ModelAndView buscarJugadores(@RequestParam(required = false) String searchText) {
        log.info("Lista de Jugadores");
        List<JugadorDTO> jugadores;
        if (searchText == null || searchText.trim().equals("")) {
            jugadores = jugadorServicio.listarJugadores();
        } else {
            jugadores = jugadorServicio.buscarJugadoresPorNombre(searchText);            
        }
        ModelAndView modelAndView = new ModelAndView("jugador-search");
        modelAndView.addObject("jugadores", jugadores);
        return modelAndView;
    }

    @GetMapping("/view/{idJugador}")   
    public ModelAndView buscarJugador(@PathVariable("idJugador") Long id){
        JugadorDTO jugador = jugadorServicio.buscarJugador(id).orElseThrow();
        List<BarcoDTO> barcosJugador = barcoServicio.obtenerBarcosPorJugador(id);
        ModelAndView modelAndView = new ModelAndView("jugador-view");
        modelAndView.addObject("jugador", jugador);
        modelAndView.addObject("barcosJugador", barcosJugador);
        return modelAndView;
    }

    @GetMapping("/create")   
    public ModelAndView formularioCrearJugador(){
        ModelAndView modelAndView = new ModelAndView("jugador-create");
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
