package co.edu.javeriana.proyectoWeb.RegataOnline.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.mapper.BarcoMapper;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Barco;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.BarcoRepositorio;

@Service
public class BarcoServicio {
    @Autowired
    private BarcoRepositorio barcoRepositorio;

     public List<BarcoDTO> listarBarcos() {
        return barcoRepositorio.findAll().stream().map(BarcoMapper::toDTO).toList();
    }

    public Optional<BarcoDTO> buscarBarco(Long id) {
        return barcoRepositorio.findById(id).map(BarcoMapper::toDTO);
    }
    
    public void guardarBarco(BarcoDTO barcoDTO) {
        Barco barco = BarcoMapper.toEntity(barcoDTO);
        barcoRepositorio.save(barco);
    }

    public void borrarBarco(long id) {
        barcoRepositorio.deleteById(id);
    }
}
