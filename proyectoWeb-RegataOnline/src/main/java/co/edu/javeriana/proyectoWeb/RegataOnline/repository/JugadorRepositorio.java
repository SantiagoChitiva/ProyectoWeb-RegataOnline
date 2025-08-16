package co.edu.javeriana.proyectoWeb.RegataOnline.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.javeriana.proyectoWeb.RegataOnline.model.Jugador;

@Repository
public interface JugadorRepositorio extends JpaRepository<Jugador, Long> {
    
}
