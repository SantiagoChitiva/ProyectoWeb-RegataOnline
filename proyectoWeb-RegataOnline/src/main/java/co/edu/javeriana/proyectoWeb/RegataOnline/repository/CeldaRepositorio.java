package co.edu.javeriana.proyectoWeb.RegataOnline.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.javeriana.proyectoWeb.RegataOnline.model.Celda;

@Repository
public interface CeldaRepositorio extends JpaRepository<Celda, Long> {
    
}
