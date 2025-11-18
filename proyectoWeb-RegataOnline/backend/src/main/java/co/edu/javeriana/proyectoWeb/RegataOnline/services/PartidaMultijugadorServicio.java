package co.edu.javeriana.proyectoWeb.RegataOnline.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.*;
import co.edu.javeriana.proyectoWeb.RegataOnline.mapper.MovimientoMapper;
import co.edu.javeriana.proyectoWeb.RegataOnline.mapper.PartidaMultijugadorMapper;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.*;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.*;


@Service
@Transactional
public class PartidaMultijugadorServicio {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PartidaRepositorio partidaRepositorio;

    @Autowired
    private PartidaJugadorRepositorio partidaJugadorRepositorio;

    @Autowired
    private MovimientoRepositorio movimientoRepositorio;

    @Autowired
    private JugadorRepositorio jugadorRepositorio;

    @Autowired
    private BarcoRepositorio barcoRepositorio;

    @Autowired
    private MapaRepositorio mapaRepositorio;

    @Autowired
    private CeldaRepositorio celdaRepositorio;

    public PartidaDTO crearPartida(CrearPartidaRequest request) {
        log.info("Creando partida multijugador para jugador {}", request.getJugadorId());
        
        Jugador jugadorCreador = jugadorRepositorio.findById(request.getJugadorId())
            .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
        
        Mapa mapa = mapaRepositorio.findById(request.getMapaId())
            .orElseThrow(() -> new RuntimeException("Mapa no encontrado"));
        
        Barco barco = barcoRepositorio.findById(request.getBarcoId())
            .orElseThrow(() -> new RuntimeException("Barco no encontrado"));

        // Crear la partida
        Partida partida = new Partida(jugadorCreador, mapa);
        partida = partidaRepositorio.save(partida);

        // Buscar celda de partida (P) para posición inicial
        Celda celdaInicial = celdaRepositorio.findByMapaIdAndTipo(mapa.getId(), "P")
            .stream().findFirst()
            .orElseThrow(() -> new RuntimeException("El mapa no tiene una celda de partida (P)"));

        // Agregar el jugador creador como primer jugador
        PartidaJugador primerJugador = new PartidaJugador(
            partida, jugadorCreador, barco, 1, 
            celdaInicial.getPosicionX(), celdaInicial.getPosicionY()
        );
        partidaJugadorRepositorio.save(primerJugador);

        partida.setCantidadJugadores(1);
        partida.setEstado("esperando");
        partidaRepositorio.save(partida);

        log.info("Partida multijugador creada con ID: {}, esperando jugadores", partida.getId());
        
        List<PartidaJugador> jugadores = partidaJugadorRepositorio.findByPartidaIdOrderByOrdenTurnoAsc(partida.getId());
        return PartidaMultijugadorMapper.toDTO(partida, jugadores);
    }

    public PartidaDTO unirseAPartida(UnirsePartidaRequest request) {
        Partida partida = partidaRepositorio.findById(request.getPartidaId())
            .orElseThrow(() -> new RuntimeException("Partida no encontrada"));

        // Validar que la partida está esperando jugadores
        if (!"esperando".equals(partida.getEstado())) {
            throw new RuntimeException("La partida ya comenzó o terminó");
        }

        // Validar que no está llena
        if (partida.getCantidadJugadores() >= partida.getMaxJugadores()) {
            throw new RuntimeException("La partida está llena");
        }

        // Validar que el jugador no esté ya en la partida
        if (partidaJugadorRepositorio.existsByPartidaIdAndJugadorId(partida.getId(), request.getJugadorId())) {
            throw new RuntimeException("El jugador ya está en esta partida");
        }

        Jugador jugador = jugadorRepositorio.findById(request.getJugadorId())
            .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
        
        Barco barco = barcoRepositorio.findById(request.getBarcoId())
            .orElseThrow(() -> new RuntimeException("Barco no encontrado"));

        // Buscar celda de partida para posición inicial
        Celda celdaInicial = celdaRepositorio.findByMapaIdAndTipo(partida.getMapa().getId(), "P")
            .stream().findFirst()
            .orElseThrow(() -> new RuntimeException("El mapa no tiene una celda de partida"));

        // Agregar jugador a la partida
        int nuevoOrden = partida.getCantidadJugadores() + 1;
        PartidaJugador nuevoJugador = new PartidaJugador(
            partida, jugador, barco, nuevoOrden,
            celdaInicial.getPosicionX(), celdaInicial.getPosicionY()
        );
        partidaJugadorRepositorio.save(nuevoJugador);

        partida.setCantidadJugadores(partida.getCantidadJugadores() + 1);
        partidaRepositorio.save(partida);

        log.info("Jugador {} unido a partida {}. Total jugadores: {}", 
            request.getJugadorId(), partida.getId(), partida.getCantidadJugadores());
        
        List<PartidaJugador> jugadores = partidaJugadorRepositorio.findByPartidaIdOrderByOrdenTurnoAsc(partida.getId());
        return PartidaMultijugadorMapper.toDTO(partida, jugadores);
    }

    public PartidaDTO iniciarPartida(Long partidaId) {
        Partida partida = partidaRepositorio.findById(partidaId)
            .orElseThrow(() -> new RuntimeException("Partida no encontrada"));

        if (!"esperando".equals(partida.getEstado())) {
            throw new RuntimeException("La partida ya está en curso o terminada");
        }

        if (partida.getCantidadJugadores() < 2) {
            throw new RuntimeException("Se necesitan al menos 2 jugadores para iniciar");
        }

        partida.setEstado("en_curso");
        partida.setOrdenTurnoActual(1);
        partidaRepositorio.save(partida);

        log.info("Partida {} iniciada con {} jugadores", partidaId, partida.getCantidadJugadores());
        
        List<PartidaJugador> jugadores = partidaJugadorRepositorio.findByPartidaIdOrderByOrdenTurnoAsc(partida.getId());
        return PartidaMultijugadorMapper.toDTO(partida, jugadores);
    }

    public MovimientoDTO realizarMovimiento(RealizarMovimientoRequest request) {
        log.info("Jugador {} realizando movimiento en partida {}", request.getJugadorId(), request.getPartidaId());
        
        Partida partida = partidaRepositorio.findById(request.getPartidaId())
            .orElseThrow(() -> new RuntimeException("Partida no encontrada"));

        // Validar que la partida está en curso
        if (!"en_curso".equals(partida.getEstado())) {
            throw new RuntimeException("La partida no está en curso");
        }

        // Obtener el jugador que quiere mover
        PartidaJugador jugadorActual = partidaJugadorRepositorio.findByPartidaIdAndJugadorId(
            partida.getId(), request.getJugadorId())
            .orElseThrow(() -> new RuntimeException("Jugador no encontrado en esta partida"));

        // Validar que es su turno
        if (!jugadorActual.getOrdenTurno().equals(partida.getOrdenTurnoActual())) {
            throw new RuntimeException("No es tu turno. Turno actual: jugador " + partida.getOrdenTurnoActual());
        }

        // Validar que el jugador no ha terminado
        if ("terminado".equals(jugadorActual.getEstado())) {
            throw new RuntimeException("Ya llegaste a la meta");
        }

        // Validar aceleración (-1, 0, +1)
        if (Math.abs(request.getAceleracionX()) > 1 || Math.abs(request.getAceleracionY()) > 1) {
            throw new RuntimeException("La aceleración debe ser -1, 0 o +1");
        }

        // Calcular física vectorial
        int velocidadXAnterior = jugadorActual.getVelocidadX();
        int velocidadYAnterior = jugadorActual.getVelocidadY();
        int posicionXAnterior = jugadorActual.getPosicionX();
        int posicionYAnterior = jugadorActual.getPosicionY();

        // Nueva velocidad = velocidad anterior + aceleración
        int velocidadXNueva = velocidadXAnterior + request.getAceleracionX();
        int velocidadYNueva = velocidadYAnterior + request.getAceleracionY();

        // Nueva posición = posición anterior + velocidad nueva
        int posicionXNueva = posicionXAnterior + velocidadXNueva;
        int posicionYNueva = posicionYAnterior + velocidadYNueva;

        // Validar que la nueva posición está dentro del mapa
        Mapa mapa = partida.getMapa();
        if (posicionXNueva < 0 || posicionXNueva >= mapa.getColumnas() || 
            posicionYNueva < 0 || posicionYNueva >= mapa.getFilas()) {
            throw new RuntimeException("Movimiento inválido: fuera del mapa");
        }

        // Verificar tipo de celda
        Celda celdaDestino = celdaRepositorio.findByMapaIdAndPosicionXAndPosicionY(
            mapa.getId(), posicionXNueva, posicionYNueva)
            .orElseThrow(() -> new RuntimeException("Celda no encontrada"));

        boolean chocoConPared = false;
        boolean llegoAMeta = false;

        if ("W".equals(celdaDestino.getTipo())) {
            // Chocó con pared - pierde el turno y se queda en la posición anterior
            chocoConPared = true;
            posicionXNueva = posicionXAnterior;
            posicionYNueva = posicionYAnterior;
            velocidadXNueva = 0;
            velocidadYNueva = 0;
        } else if ("M".equals(celdaDestino.getTipo())) {
            // Llegó a la meta
            llegoAMeta = true;
            jugadorActual.setHaLlegadoMeta(true);
            jugadorActual.setEstado("terminado");
            
            // Asignar posición final
            long jugadoresTerminados = partidaJugadorRepositorio.findByPartidaId(partida.getId())
                .stream().filter(pj -> pj.getHaLlegadoMeta()).count();
            jugadorActual.setPosicionFinal((int) jugadoresTerminados);

            // Si es el primero en llegar, es el ganador
            if (jugadoresTerminados == 1) {
                partida.setGanador(jugadorActual.getJugador());
                partida.setEstado("terminada");
                partida.setFechaFin(LocalDateTime.now());
            }
        }

        // Actualizar estado del jugador
        jugadorActual.setPosicionX(posicionXNueva);
        jugadorActual.setPosicionY(posicionYNueva);
        jugadorActual.setVelocidadX(velocidadXNueva);
        jugadorActual.setVelocidadY(velocidadYNueva);
        jugadorActual.setMovimientosRealizados(jugadorActual.getMovimientosRealizados() + 1);
        partidaJugadorRepositorio.save(jugadorActual);

        // Registrar el movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setPartidaJugador(jugadorActual);
        movimiento.setNumeroTurno(partida.getNumeroTurnoActual());
        movimiento.setAceleracionX(request.getAceleracionX());
        movimiento.setAceleracionY(request.getAceleracionY());
        movimiento.setVelocidadXAnterior(velocidadXAnterior);
        movimiento.setVelocidadYAnterior(velocidadYAnterior);
        movimiento.setVelocidadXNueva(velocidadXNueva);
        movimiento.setVelocidadYNueva(velocidadYNueva);
        movimiento.setPosicionXAnterior(posicionXAnterior);
        movimiento.setPosicionYAnterior(posicionYAnterior);
        movimiento.setPosicionXNueva(posicionXNueva);
        movimiento.setPosicionYNueva(posicionYNueva);
        movimiento.setLlegoAMeta(llegoAMeta);
        movimiento.setChocoConPared(chocoConPared);
        movimiento = movimientoRepositorio.save(movimiento);

        // Pasar al siguiente turno si la partida no ha terminado
        if (!"terminada".equals(partida.getEstado())) {
            int siguienteOrden = partida.getOrdenTurnoActual() + 1;
            if (siguienteOrden > partida.getCantidadJugadores()) {
                siguienteOrden = 1;
                partida.setNumeroTurnoActual(partida.getNumeroTurnoActual() + 1);
            }
            partida.setOrdenTurnoActual(siguienteOrden);
            partida.setFechaUltimaJugada(LocalDateTime.now());
        }

        partidaRepositorio.save(partida);

        log.info("Movimiento registrado. Partida: {}, Turno: {}, Siguiente orden: {}", 
            partida.getId(), partida.getNumeroTurnoActual(), partida.getOrdenTurnoActual());
        
        return MovimientoMapper.toDTO(movimiento);
    }

    public PartidaDTO obtenerPartida(Long partidaId) {
        Partida partida = partidaRepositorio.findById(partidaId)
            .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
        
        List<PartidaJugador> jugadores = partidaJugadorRepositorio.findByPartidaIdOrderByOrdenTurnoAsc(partida.getId());
        return PartidaMultijugadorMapper.toDTO(partida, jugadores);
    }

    public List<PartidaDTO> listarPartidasDisponibles() {
        return partidaRepositorio.findAll().stream()
            .filter(p -> "esperando".equals(p.getEstado()))
            .filter(p -> p.getCantidadJugadores() < p.getMaxJugadores())
            .map(p -> {
                List<PartidaJugador> jugadores = partidaJugadorRepositorio.findByPartidaIdOrderByOrdenTurnoAsc(p.getId());
                return PartidaMultijugadorMapper.toDTO(p, jugadores);
            })
            .collect(Collectors.toList());
    }

    public List<MovimientoDTO> obtenerMovimientosJugador(Long partidaId, Long jugadorId) {
        PartidaJugador partidaJugador = partidaJugadorRepositorio.findByPartidaIdAndJugadorId(partidaId, jugadorId)
            .orElseThrow(() -> new RuntimeException("Jugador no encontrado en esta partida"));

        return movimientoRepositorio.findByPartidaJugadorIdOrderByNumeroTurnoAsc(partidaJugador.getId())
            .stream()
            .map(MovimientoMapper::toDTO)
            .collect(Collectors.toList());
    }
}
