package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoJugadorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.BarcoServicio;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.JugadorServicio;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/jugador/barcos")
public class BarcoJugadorControlador {
    @Autowired
    private JugadorServicio jugadorServicio;

    @Autowired
    private BarcoServicio barcoServicio;

    @GetMapping("/edit/{jugadorId}")
    public ModelAndView editBarcosJugador(@PathVariable Long jugadorId) {
        BarcoJugadorDTO barcoJugadorDTO = barcoServicio.getBarcoJugador(jugadorId).orElseThrow();
        List<BarcoDTO> allBarcos = barcoServicio.listarBarcos();

        ModelAndView modelAndView = new ModelAndView("barco-jugador-edit");
        modelAndView.addObject("barcoJugador", barcoJugadorDTO);
        modelAndView.addObject("allBarcos", allBarcos);
        return modelAndView;
    }

    @PostMapping("/save")
    public RedirectView saveBarcosJugador(@ModelAttribute BarcoJugadorDTO barcoJugadorDTO) {
        barcoServicio.updateBarcosJugador(barcoJugadorDTO);
        return new RedirectView("/jugador/list");
    }
    
    
}
