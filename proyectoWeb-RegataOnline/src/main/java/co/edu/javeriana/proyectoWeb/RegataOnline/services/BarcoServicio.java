package co.edu.javeriana.proyectoWeb.RegataOnline.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.javeriana.proyectoWeb.RegataOnline.model.Barco;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.BarcoRepositorio;

@Service
public class BarcoServicio {
    @Autowired
    private BarcoRepositorio barcoRepositorio;

     public List<Barco> listarBarcos() {
        return barcoRepositorio.findAll();
    }

    public Barco buscarBarco(Long id) {
        return barcoRepositorio.findById(id).orElseThrow();
    }
    
}
