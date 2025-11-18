package co.edu.javeriana.proyectoWeb.RegataOnline.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.javeriana.proyectoWeb.RegataOnline.model.PartidaJugador;

@Repository
public interface PartidaJugadorRepositorio extends JpaRepository<PartidaJugador, Long> {
    
    List<PartidaJugador> findByPartidaId(Long partidaId);
    
    List<PartidaJugador> findByPartidaIdOrderByOrdenTurnoAsc(Long partidaId);
    
    Optional<PartidaJugador> findByPartidaIdAndJugadorId(Long partidaId, Long jugadorId);
    
    Optional<PartidaJugador> findByPartidaIdAndOrdenTurno(Long partidaId, Integer ordenTurno);
    
    long countByPartidaId(Long partidaId);
    
    boolean existsByPartidaIdAndJugadorId(Long partidaId, Long jugadorId);
}
