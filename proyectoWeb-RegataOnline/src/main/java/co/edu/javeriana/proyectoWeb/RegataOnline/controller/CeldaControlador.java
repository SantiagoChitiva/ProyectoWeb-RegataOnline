package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.CeldaDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.BarcoServicio;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.CeldaServicio;

@Controller
@RequestMapping("/celda")
public class CeldaControlador {
    
    @Autowired 
    private CeldaServicio celdaServicio;
    
    @Autowired
    private BarcoServicio barcoServicio;

    private Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/list")
    public ModelAndView listarCeldas() {
        List<CeldaDTO> celdas = celdaServicio.listarCeldas();
        ModelAndView modelAndView = new ModelAndView("celda-lista");
        modelAndView.addObject("listaCeldas", celdas);
        return modelAndView;
    }

    @GetMapping("/view/{id}")
    public ModelAndView buscarCelda(@PathVariable("id") Long id){
        CeldaDTO celda = celdaServicio.buscarCelda(id).orElseThrow();
        List<BarcoDTO> barcosCelda = barcoServicio.obtenerBarcosPorCelda(id);
        ModelAndView modelAndView = new ModelAndView("celda-view");
        modelAndView.addObject("celda", celda);
        modelAndView.addObject("barcosCelda", barcosCelda);
        return modelAndView;
    }
}