package co.edu.javeriana.proyectoWeb.RegataOnline.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.javeriana.proyectoWeb.RegataOnline.model.Jugador;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Partida;

@Repository
public interface PartidaRepositorio extends JpaRepository<Partida, Long> {
    
    /**
     * Busca partidas activas o pausadas de un jugador
     */
    Optional<Partida> findByJugadorAndEstadoIn(Jugador jugador, List<String> estados);
    
    /**
     * Busca todas las partidas de un jugador
     */
    List<Partida> findByJugador(Jugador jugador);
    
    /**
     * Busca partidas activas de un jugador
     */
    Optional<Partida> findByJugadorAndEstado(Jugador jugador, String estado);
}
