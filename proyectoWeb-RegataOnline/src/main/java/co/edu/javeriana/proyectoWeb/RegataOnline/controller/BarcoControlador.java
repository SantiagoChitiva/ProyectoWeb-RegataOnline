package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.CeldaDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.JugadorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.ModeloDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.BarcoServicio;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.CeldaServicio;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.JugadorServicio;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.ModeloServicio;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/barco")    
public class BarcoControlador {
    @Autowired 
    private BarcoServicio barcoServicio;
    @Autowired
    private ModeloServicio modeloServicio;
    @Autowired
    private JugadorServicio jugadorServicio;
    @Autowired
    private CeldaServicio celdaServicio;

    private Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/list")
    public ModelAndView listarBarcos() {
        List<BarcoDTO> barcos = barcoServicio.listarBarcos();
        ModelAndView modelAndView = new ModelAndView("barco-lista");
        modelAndView.addObject("listaBarcos", barcos);
        return modelAndView;
    }

    @GetMapping("/view/{id}")
    public ModelAndView buscarBarco(@PathVariable("id") Long id){
        BarcoDTO barco = barcoServicio.buscarBarco(id).orElseThrow();
        ModelAndView modelAndView = new ModelAndView("barco-view");
        modelAndView.addObject("barco", barco);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView formularioCrearBarco() {
        List<ModeloDTO> modelos = modeloServicio.listarModelos();
        List<JugadorDTO> jugadores = jugadorServicio.listarJugadores();
        List<CeldaDTO> celdas = celdaServicio.listarCeldas();

        ModelAndView modelAndView = new ModelAndView("barco-edit");
        modelAndView.addObject("barco", new BarcoDTO());
        modelAndView.addObject("modelosBarco", modelos);
        modelAndView.addObject("jugadores", jugadores);
        modelAndView.addObject("celdas", celdas);
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView formularioEditarBarco(@PathVariable Long id) {
        BarcoDTO barcoDTO = barcoServicio.buscarBarco(id).orElseThrow();
        List<ModeloDTO> modelos = modeloServicio.listarModelos();
        List<JugadorDTO> jugadores = jugadorServicio.listarJugadores();
        List<CeldaDTO> celdas = celdaServicio.listarCeldas();

        ModelAndView modelAndView = new ModelAndView("barco-edit");
        modelAndView.addObject("barco", barcoDTO);
        modelAndView.addObject("modelosBarco", modelos);
        modelAndView.addObject("jugadores", jugadores);
        modelAndView.addObject("celdas", celdas);
        return modelAndView;
    }

    @PostMapping("/save")
    public RedirectView guardarBarco(@ModelAttribute BarcoDTO barcoDTO) {
        barcoServicio.guardarBarco(barcoDTO);
        return new RedirectView("/barco/list");
    }

    @GetMapping("/delete/{id}")
    public RedirectView borrarBarco(@PathVariable long id) {
        barcoServicio.borrarBarco(id);
        return new RedirectView("/barco/list");
    }
}
