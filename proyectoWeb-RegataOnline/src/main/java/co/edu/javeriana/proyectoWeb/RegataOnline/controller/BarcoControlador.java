package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.CeldaDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.ErrorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.JugadorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.ModeloDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.BarcoServicio;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.CeldaServicio;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.JugadorServicio;
import co.edu.javeriana.proyectoWeb.RegataOnline.services.ModeloServicio;

import java.util.List;

import javax.print.DocFlavor.READER;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@RestController
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
    public List<BarcoDTO> listarBarcos() {
        return barcoServicio.listarBarcos();
    }

    @GetMapping("/list/{page}")
    public ResponseEntity<?> listarBarcos(@PathVariable Integer page) {
        if (page > 0) {
            return ResponseEntity.ok(barcoServicio.listarBarcos(PageRequest.of(page, 10)));    
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO("El numero de pagina debe ser mayor a 0"));
        }
        
    }

    @GetMapping("/search")
    public ResponseEntity<?> buscarBarcos(@RequestParam(required = false) String searchText) {
        // Validar que el texto de búsqueda no sea solo espacios en blanco
        if (searchText != null && searchText.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDTO("El texto de búsqueda no puede estar vacío"));
        }
        
        if (searchText == null || searchText.trim().equals("")) {
            return ResponseEntity.ok(barcoServicio.listarBarcos());
        } else {
            return ResponseEntity.ok(barcoServicio.buscarBarcosPorNombre(searchText.trim()));
        }
    }

    @GetMapping("{id}")
    public BarcoDTO buscarBarco(@PathVariable("id") Long id){
        return barcoServicio.buscarBarco(id).orElseThrow();
    }

    @PostMapping
    public BarcoDTO crearBarco(@RequestBody BarcoDTO barcoDTO) {
        return barcoServicio.crearBarco(barcoDTO);
    }

    @PutMapping
    public BarcoDTO actualizarBarco(@RequestBody BarcoDTO barcoDTO) {
        return barcoServicio.actualizarBarco(barcoDTO);
    }

    @DeleteMapping("{id}")
    public void borrarBarco(@PathVariable long id) {
        barcoServicio.borrarBarco(id);
    }
}
