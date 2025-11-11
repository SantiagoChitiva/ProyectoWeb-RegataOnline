package co.edu.javeriana.proyectoWeb.RegataOnline.repository;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.JugadorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Jugador;

@Repository
public interface JugadorRepositorio extends JpaRepository<Jugador, Long> {

    List<Jugador> findByNombreContainingIgnoreCase(String searchText);
    
    java.util.Optional<Jugador> findByEmail(String email);
    
}
