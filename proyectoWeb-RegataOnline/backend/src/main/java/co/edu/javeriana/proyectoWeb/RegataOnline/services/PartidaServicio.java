package co.edu.javeriana.proyectoWeb.RegataOnline.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.CrearPartidaRequest;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.PartidaDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.mapper.PartidaMapper;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Barco;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Jugador;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Mapa;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Partida;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.BarcoRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.JugadorRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.MapaRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.PartidaRepositorio;

@Service
public class PartidaServicio {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PartidaRepositorio partidaRepositorio;

    @Autowired
    private JugadorRepositorio jugadorRepositorio;

    @Autowired
    private MapaRepositorio mapaRepositorio;

    @Autowired
    private BarcoRepositorio barcoRepositorio;

    /**
     * Crea una nueva partida
     */
    @Transactional
    public PartidaDTO crearPartida(CrearPartidaRequest request) {
        log.info("Creando nueva partida para jugador {}", request.getJugadorId());

        // Validar que no exista una partida activa para este jugador
        Jugador jugador = jugadorRepositorio.findById(request.getJugadorId())
            .orElseThrow(() -> new RuntimeException("Jugador no encontrado con ID: " + request.getJugadorId()));

        Optional<Partida> partidaExistente = partidaRepositorio.findByJugadorAndEstadoIn(
            jugador, 
            Arrays.asList("activa", "pausada")
        );

        if (partidaExistente.isPresent()) {
            throw new RuntimeException("El jugador ya tiene una partida activa. Debe finalizarla antes de crear una nueva.");
        }

        // Validar mapa
        Mapa mapa = mapaRepositorio.findById(request.getMapaId())
            .orElseThrow(() -> new RuntimeException("Mapa no encontrado con ID: " + request.getMapaId()));

        // Validar barco
        Barco barco = barcoRepositorio.findById(request.getBarcoId())
            .orElseThrow(() -> new RuntimeException("Barco no encontrado con ID: " + request.getBarcoId()));

        // Validar que el barco pertenece al jugador
        if (barco.getJugador() != null && !barco.getJugador().getId().equals(jugador.getId())) {
            throw new RuntimeException("El barco no pertenece a este jugador");
        }

        // Crear partida
        Partida partida = new Partida(jugador, mapa, barco);
        partida = partidaRepositorio.save(partida);

        log.info("Partida creada exitosamente con ID: {}", partida.getId());
        return PartidaMapper.toDTO(partida);
    }

    /**
     * Busca una partida activa del jugador
     */
    public Optional<PartidaDTO> buscarPartidaActiva(Long jugadorId) {
        log.info("Buscando partida activa para jugador {}", jugadorId);

        Jugador jugador = jugadorRepositorio.findById(jugadorId)
            .orElseThrow(() -> new RuntimeException("Jugador no encontrado con ID: " + jugadorId));

        return partidaRepositorio.findByJugadorAndEstadoIn(jugador, Arrays.asList("activa", "pausada"))
            .map(PartidaMapper::toDTO);
    }

    /**
     * Obtiene una partida por ID
     */
    public Optional<PartidaDTO> obtenerPartida(Long id) {
        return partidaRepositorio.findById(id)
            .map(PartidaMapper::toDTO);
    }

    /**
     * Lista todas las partidas de un jugador
     */
    public List<PartidaDTO> listarPartidasJugador(Long jugadorId) {
        Jugador jugador = jugadorRepositorio.findById(jugadorId)
            .orElseThrow(() -> new RuntimeException("Jugador no encontrado con ID: " + jugadorId));

        return partidaRepositorio.findByJugador(jugador).stream()
            .map(PartidaMapper::toDTO)
            .toList();
    }

    /**
     * Pausa una partida
     */
    @Transactional
    public PartidaDTO pausarPartida(Long id) {
        log.info("Pausando partida {}", id);

        Partida partida = partidaRepositorio.findById(id)
            .orElseThrow(() -> new RuntimeException("Partida no encontrada con ID: " + id));

        partida.setEstado("pausada");
        partida = partidaRepositorio.save(partida);

        return PartidaMapper.toDTO(partida);
    }

    /**
     * Finaliza una partida
     */
    @Transactional
    public PartidaDTO finalizarPartida(Long id) {
        log.info("Finalizando partida {}", id);

        Partida partida = partidaRepositorio.findById(id)
            .orElseThrow(() -> new RuntimeException("Partida no encontrada con ID: " + id));

        partida.setEstado("terminada");
        partida = partidaRepositorio.save(partida);

        return PartidaMapper.toDTO(partida);
    }

    /**
     * Actualiza la posición del barco y cuenta movimientos
     */
    @Transactional
    public PartidaDTO moverBarco(Long partidaId, Integer nuevaX, Integer nuevaY) {
        log.info("Moviendo barco en partida {} a posición ({}, {})", partidaId, nuevaX, nuevaY);

        Partida partida = partidaRepositorio.findById(partidaId)
            .orElseThrow(() -> new RuntimeException("Partida no encontrada con ID: " + partidaId));

        if (!partida.getEstado().equals("activa")) {
            throw new RuntimeException("La partida no está activa");
        }

        // Actualizar posición del barco
        Barco barco = partida.getBarco();
        barco.setPosicionX(nuevaX);
        barco.setPosicionY(nuevaY);
        barcoRepositorio.save(barco);

        // Incrementar contador de movimientos
        partida.setMovimientos(partida.getMovimientos() + 1);
        partida = partidaRepositorio.save(partida);

        log.info("Barco movido. Movimientos totales: {}", partida.getMovimientos());
        return PartidaMapper.toDTO(partida);
    }
}
