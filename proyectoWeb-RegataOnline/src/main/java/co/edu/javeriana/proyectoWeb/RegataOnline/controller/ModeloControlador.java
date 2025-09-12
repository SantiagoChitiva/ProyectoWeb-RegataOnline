package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.JugadorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.ModeloDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.BarcoServicio;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.ModeloServicio;

@Controller
@RequestMapping("/modelo")
public class ModeloControlador {
    
    @Autowired
    private ModeloServicio modeloServicio;
    
    @Autowired
    private BarcoServicio barcoServicio;

    private Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/list")
    public ModelAndView listarModelos() {
        log.info("Lista de Modelos");
        List<ModeloDTO> modelos = modeloServicio.listarModelos();
        ModelAndView modelAndView = new ModelAndView("modelo-lista");
        modelAndView.addObject("listaModelos", modelos);
        return modelAndView;
    }

    @GetMapping("/list/{page}")
    public ModelAndView listarModelos(@PathVariable Integer page) {
        log.info("Lista de Modelos");
        List<ModeloDTO> modelos = modeloServicio.listarModelos(PageRequest.of(page, 2));
        ModelAndView modelAndView = new ModelAndView("modelo-lista");
        modelAndView.addObject("listaModelos", modelos);
        return modelAndView;
    }

    @GetMapping("/search")
    public ModelAndView buscarModelos(@RequestParam(required = false) String searchText) {
        log.info("Lista de Modelos");
        List<ModeloDTO> modelos;
        if (searchText == null || searchText.trim().equals("")) {
            modelos = modeloServicio.listarModelos();
        } else {
            modelos = modeloServicio.buscarModelosPorNombre(searchText);            
        }
        ModelAndView modelAndView = new ModelAndView("modelo-search");
        modelAndView.addObject("listaModelos", modelos);
        return modelAndView;
    }

    @GetMapping("/view/{id}")
    public ModelAndView buscarModelo(@PathVariable("id") Long id) {
        ModeloDTO modelo = modeloServicio.buscarModelo(id).orElseThrow();
        List<BarcoDTO> barcosModelo = barcoServicio.obtenerBarcosPorModelo(id);
        ModelAndView modelAndView = new ModelAndView("modelo-view");
        modelAndView.addObject("modelo", modelo);
        modelAndView.addObject("barcosModelo", barcosModelo);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView formularioCrearModelo() {
        ModelAndView modelAndView = new ModelAndView("modelo-edit");
        modelAndView.addObject("modelo", new ModeloDTO());
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView formularioEditarModelo(@PathVariable Long id) {
        ModeloDTO modeloDTO = modeloServicio.buscarModelo(id).orElseThrow();
        ModelAndView modelAndView = new ModelAndView("modelo-edit");
        modelAndView.addObject("modelo", modeloDTO);
        return modelAndView;
    }

    @PostMapping("/save")
    public RedirectView guardarModelo(@ModelAttribute ModeloDTO modeloDTO) {
        modeloServicio.guardarModelo(modeloDTO);
        return new RedirectView("/modelo/list");
    }

    @GetMapping("/delete/{id}")
    public RedirectView borrarModelo(@PathVariable long id) {
        modeloServicio.borrarModelo(id);
        return new RedirectView("/modelo/list");
    }
}