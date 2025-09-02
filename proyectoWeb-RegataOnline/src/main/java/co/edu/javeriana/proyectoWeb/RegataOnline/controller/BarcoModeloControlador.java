package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoJugadorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoModeloDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.ModeloDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.BarcoServicio;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.ModeloServicio;

@Controller
@RequestMapping("/barco/modelos")
public class BarcoModeloControlador {
    @Autowired
    BarcoServicio barcoServicio;

    @Autowired
    ModeloServicio modeloServicio;

    @GetMapping("/edit/{barcoId}")
    public ModelAndView editBarcoModelo(@PathVariable Long barcoId) {
        BarcoModeloDTO barcoModeloDTO = barcoServicio.getBarcoModelo(barcoId).orElseThrow();
        List<ModeloDTO> allModelos = modeloServicio.listarModelos();

        ModelAndView modelAndView = new ModelAndView("barco-modelo-edit");
        modelAndView.addObject("barcoModelo", barcoModeloDTO);
        modelAndView.addObject("allModelos", allModelos);
        return modelAndView;
    }
/*
    @PostMapping("/save")
    public RedirectView saveBarcosJugador(@ModelAttribute BarcoJugadorDTO barcoJugadorDTO) {
        barcoServicio.updateBarcosJugador(barcoJugadorDTO);
        return new RedirectView("/jugador/edit/" + barcoJugadorDTO.getJugadorId());
    }

*/  
}
