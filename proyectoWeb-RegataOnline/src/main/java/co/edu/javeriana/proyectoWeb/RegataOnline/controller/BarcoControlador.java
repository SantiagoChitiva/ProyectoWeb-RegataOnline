package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import co.edu.javeriana.proyectoWeb.RegataOnline.model.Barco;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.BarcoServicio;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/barco")    
public class BarcoControlador {
    @Autowired 
    private BarcoServicio barcoServicio;

    private Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/list")
    public ModelAndView listarBarcos() {
        List<Barco> barcos = barcoServicio.listarBarcos();
        ModelAndView modelAndView = new ModelAndView("barco-lista");
        modelAndView.addObject("listaBarcos", barcos);
        return modelAndView;
    }

    @GetMapping("/view/{idBarco}")
    public ModelAndView buscarBarco(@PathVariable("idBarco") Long id){
        Barco barco = barcoServicio.buscarBarco(id);
        ModelAndView modelAndView = new ModelAndView("barco-view");
        modelAndView.addObject("barco", barco);
        return modelAndView;
    }
    

    
}
