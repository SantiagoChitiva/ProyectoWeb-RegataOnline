package co.edu.javeriana.proyectoWeb.RegataOnline.services;

import java.util.List;
import java.util.Optional;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.ModeloRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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

    public List<ModeloDTO> listarModelos(Pageable pageable) {
        return modeloRepositorio.findAll(pageable).stream().map(ModeloMapper::toDTO).toList();
    }

    public Optional<ModeloDTO> buscarModelo(Long id) {
        return modeloRepositorio.findById(id).map(ModeloMapper::toDTO);
    }
    
    public ModeloDTO guardarModelo(ModeloDTO modeloDTO) {
        Modelo modelo = ModeloMapper.toEntity(modeloDTO);
        return ModeloMapper.toDTO(modeloRepositorio.save(modelo));
    }

    public void borrarModelo(long id) {
        Optional<Modelo> modeloOpt = modeloRepositorio.findById(id);
        if (modeloOpt.isPresent()) {
            Modelo modelo = modeloOpt.get();

            // Desasociar todos los barcos de este modelo antes de eliminarlo
            for (Barco barco : modelo.getBarcos()) {
                barco.setModelo(null);
            }

            modelo.getBarcos().clear();
            modeloRepositorio.save(modelo);
            modeloRepositorio.deleteById(id);
        }
    }

    public List<BarcoDTO> obtenerBarcosPorModelo(Long modeloId) {
        Optional<Modelo> modeloOpt = modeloRepositorio.findById(modeloId);
        
        if (modeloOpt.isEmpty()) {
            return List.of();
        }
        
        Modelo modelo = modeloOpt.get();
        return modelo.getBarcos().stream().map(BarcoMapper::toDTO).toList();
    }

    public List<ModeloDTO> buscarModelosPorNombre(String searchText) {
        return modeloRepositorio.findBynombreModeloContainingIgnoreCase(searchText).stream().map(ModeloMapper::toDTO).toList();   
    }
}
