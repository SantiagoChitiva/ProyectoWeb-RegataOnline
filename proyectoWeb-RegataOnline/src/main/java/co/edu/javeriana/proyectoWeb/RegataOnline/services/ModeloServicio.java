package co.edu.javeriana.proyectoWeb.RegataOnline.services;

import java.util.List;
import java.util.Optional;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.ModeloRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.ModeloDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.mapper.BarcoMapper;
import co.edu.javeriana.proyectoWeb.RegataOnline.mapper.ModeloMapper;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Barco;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Modelo;

@Service
public class ModeloServicio {

    @Autowired
    private ModeloRepositorio modeloRepositorio;

    public List<ModeloDTO> listarModelos() {
        return modeloRepositorio.findAll().stream().map(ModeloMapper::toDTO).toList();
    }

    public Optional<ModeloDTO> buscarModelo(Long id) {
        return modeloRepositorio.findById(id).map(ModeloMapper::toDTO);
    }
    
    public void guardarModelo(ModeloDTO modeloDTO) {
        Modelo modelo = ModeloMapper.toEntity(modeloDTO);
        modeloRepositorio.save(modelo);
    }

    public void borrarModelo(long id) {
        modeloRepositorio.deleteById(id);
    }

}
