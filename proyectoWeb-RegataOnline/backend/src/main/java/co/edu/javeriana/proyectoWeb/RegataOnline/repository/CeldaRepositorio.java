package co.edu.javeriana.proyectoWeb.RegataOnline.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.javeriana.proyectoWeb.RegataOnline.model.Celda;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Mapa;

@Repository
public interface CeldaRepositorio extends JpaRepository<Celda, Long> {
    List<Celda> findByMapa(Mapa mapa);
    
    
    List<Celda> findByMapaIdAndTipo(Long mapaId, String tipo);
    Optional<Celda> findByMapaIdAndPosicionXAndPosicionY(Long mapaId, Integer posicionX, Integer posicionY);
}
