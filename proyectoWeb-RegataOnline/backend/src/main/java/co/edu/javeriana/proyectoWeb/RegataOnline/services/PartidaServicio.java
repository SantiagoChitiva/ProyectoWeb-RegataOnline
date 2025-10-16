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
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Celda;
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

        // Buscar la celda de partida (tipo 'P') en el mapa
        Celda celdaPartida = mapa.getCeldas().stream()
            .filter(c -> "P".equals(c.getTipo()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("El mapa no tiene una celda de partida (tipo 'P')"));

        // Colocar el barco en la posición de partida con velocidad (0,0)
        barco.setPosicionX(celdaPartida.getPosicionX());
        barco.setPosicionY(celdaPartida.getPosicionY());
        barco.setVelocidadX(0);
        barco.setVelocidadY(0);
        barcoRepositorio.save(barco);

        log.info("Barco colocado en posición de partida: ({}, {})", 
            celdaPartida.getPosicionX(), celdaPartida.getPosicionY());

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
     * Mueve el barco aplicando cambios de aceleración a la velocidad
     * @param partidaId ID de la partida
     * @param aceleracionX Cambio en velocidadX (-1, 0, o +1)
     * @param aceleracionY Cambio en velocidadY (-1, 0, o +1)
     */
    @Transactional
    public PartidaDTO moverBarco(Long partidaId, Integer aceleracionX, Integer aceleracionY) {
        log.info("Moviendo barco en partida {} con aceleración ({}, {})", partidaId, aceleracionX, aceleracionY);

        // Validar aceleraciones
        if (aceleracionX < -1 || aceleracionX > 1 || aceleracionY < -1 || aceleracionY > 1) {
            throw new RuntimeException("La aceleración solo puede ser -1, 0, o +1");
        }

        Partida partida = partidaRepositorio.findById(partidaId)
            .orElseThrow(() -> new RuntimeException("Partida no encontrada con ID: " + partidaId));

        if (!partida.getEstado().equals("activa")) {
            throw new RuntimeException("La partida no está activa");
        }

        Barco barco = partida.getBarco();
        Mapa mapa = partida.getMapa();

        log.info("=== ESTADO ANTES DEL MOVIMIENTO ===");
        log.info("Mapa: {}x{} con {} celdas", mapa.getFilas(), mapa.getColumnas(), 
            mapa.getCeldas() != null ? mapa.getCeldas().size() : 0);
        log.info("Posición actual del barco: ({}, {})", barco.getPosicionX(), barco.getPosicionY());
        log.info("Velocidad actual del barco: ({}, {})", barco.getVelocidadX(), barco.getVelocidadY());
        log.info("Aceleración recibida: ({}, {})", aceleracionX, aceleracionY);

        // 1. Actualizar velocidad con la aceleración
        int nuevaVelocidadX = barco.getVelocidadX() + aceleracionX;
        int nuevaVelocidadY = barco.getVelocidadY() + aceleracionY;

        log.info("Nueva velocidad calculada: ({}, {})", nuevaVelocidadX, nuevaVelocidadY);

        // 2. Calcular nueva posición: posición_actual + velocidad_nueva
        int nuevaPosicionX = barco.getPosicionX() + nuevaVelocidadX;
        int nuevaPosicionY = barco.getPosicionY() + nuevaVelocidadY;

        log.info("Nueva posición calculada: ({}, {})", nuevaPosicionX, nuevaPosicionY);

        // 3. Validar que la nueva posición esté dentro del mapa
        if (nuevaPosicionX < 0 || nuevaPosicionX >= mapa.getColumnas() || 
            nuevaPosicionY < 0 || nuevaPosicionY >= mapa.getFilas()) {
            throw new RuntimeException("Movimiento fuera del mapa. Nueva posición: (" + 
                nuevaPosicionX + ", " + nuevaPosicionY + ")");
        }

        // 4. Validar que la celda destino no sea una pared
        Celda celdaDestino = mapa.getCeldas().stream()
            .filter(c -> c.getPosicionX() == nuevaPosicionX && c.getPosicionY() == nuevaPosicionY)
            .findFirst()
            .orElse(null);

        if (celdaDestino != null && celdaDestino.getTipo() != null && celdaDestino.getTipo().equals("x")) {
            throw new RuntimeException("No puedes moverte a una pared");
        }

        // 5. Actualizar velocidad y posición del barco
        barco.setVelocidadX(nuevaVelocidadX);
        barco.setVelocidadY(nuevaVelocidadY);
        barco.setPosicionX(nuevaPosicionX);
        barco.setPosicionY(nuevaPosicionY);
        barcoRepositorio.save(barco);

        // 6. Verificar si llegó a la meta
        if (celdaDestino != null && celdaDestino.getTipo() != null && celdaDestino.getTipo().equals("M")) {
            partida.setHaLlegadoMeta(true);
            partida.setEstado("terminada");
            log.info("¡Barco llegó a la meta!");
        }

        // 7. Incrementar contador de movimientos
        partida.setMovimientos(partida.getMovimientos() + 1);
        partida = partidaRepositorio.save(partida);

        log.info("Barco movido. Velocidad: ({}, {}), Posición: ({}, {}), Movimientos: {}", 
            nuevaVelocidadX, nuevaVelocidadY, nuevaPosicionX, nuevaPosicionY, partida.getMovimientos());
        
        return PartidaMapper.toDTO(partida);
    }
}
