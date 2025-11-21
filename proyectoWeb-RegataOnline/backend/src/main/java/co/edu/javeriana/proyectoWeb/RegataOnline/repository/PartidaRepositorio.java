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
     * Busca partidas activas o pausadas de un jugador (modo single player)
     * Actualizado para trabajar con jugadorCreador
     */
    Optional<Partida> findByJugadorCreadorAndEstadoIn(Jugador jugadorCreador, List<String> estados);
    
    /**
     * Busca todas las partidas de un jugador
     */
    List<Partida> findByJugadorCreador(Jugador jugadorCreador);
    
    /**
     * Busca partidas activas de un jugador
     */
    Optional<Partida> findByJugadorCreadorAndEstado(Jugador jugadorCreador, String estado);
    
    // Métodos legacy (deprecados, mantener por compatibilidad)
    @Deprecated
    default Optional<Partida> findByJugadorAndEstadoIn(Jugador jugador, List<String> estados) {
        return findByJugadorCreadorAndEstadoIn(jugador, estados);
    }
    
    @Deprecated
    default List<Partida> findByJugador(Jugador jugador) {
        return findByJugadorCreador(jugador);
    }
    
    @Deprecated
    default Optional<Partida> findByJugadorAndEstado(Jugador jugador, String estado) {
        return findByJugadorCreadorAndEstado(jugador, estado);
    }
}
