package co.edu.javeriana.proyectoWeb.RegataOnline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.javeriana.proyectoWeb.RegataOnline.model.Barco;

@Repository
public interface BarcoRepositorio extends JpaRepository<Barco, Long> {
    
}
