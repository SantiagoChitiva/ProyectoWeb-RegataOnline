package co.edu.javeriana.proyectoWeb.RegataOnline.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.javeriana.proyectoWeb.RegataOnline.model.Movimiento;

@Repository
public interface MovimientoRepositorio extends JpaRepository<Movimiento, Long> {
    
    List<Movimiento> findByPartidaJugadorIdOrderByNumeroTurnoAsc(Long partidaJugadorId);
    
    List<Movimiento> findByPartidaJugadorPartidaIdOrderByNumeroTurnoAsc(Long partidaId);
    
    long countByPartidaJugadorId(Long partidaJugadorId);
}
