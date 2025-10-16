package co.edu.javeriana.proyectoWeb.RegataOnline.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.javeriana.proyectoWeb.RegataOnline.model.Modelo;

@Repository
public interface ModeloRepositorio extends JpaRepository<Modelo, Long> {
    
    List<Modelo> findBynombreModeloContainingIgnoreCase(String searchText);
}
