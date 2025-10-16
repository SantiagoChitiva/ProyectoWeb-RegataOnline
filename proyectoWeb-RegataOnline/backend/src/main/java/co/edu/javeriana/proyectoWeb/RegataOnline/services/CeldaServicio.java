package co.edu.javeriana.proyectoWeb.RegataOnline.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.CeldaDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.mapper.CeldaMapper;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Celda;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.CeldaRepositorio;

@Service
public class CeldaServicio {
    
    @Autowired
    private CeldaRepositorio celdaRepositorio;

    public List<CeldaDTO> listarCeldas() {
        return celdaRepositorio.findAll().stream().map(CeldaMapper::toDTO).toList();
    }

    public Optional<CeldaDTO> buscarCelda(Long id) {
        return celdaRepositorio.findById(id).map(CeldaMapper::toDTO);
    }
}