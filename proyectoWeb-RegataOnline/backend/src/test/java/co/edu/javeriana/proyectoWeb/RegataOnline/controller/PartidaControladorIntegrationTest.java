package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.CrearPartidaRequest;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.PartidaDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Barco;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Celda;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Jugador;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Mapa;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.BarcoRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.CeldaRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.JugadorRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.MapaRepositorio;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration-testing")
public class PartidaControladorIntegrationTest {

    private String SERVER_URL;

    @Autowired
    private JugadorRepositorio jugadorRepositorio;

    @Autowired
    private MapaRepositorio mapaRepositorio;

    @Autowired
    private CeldaRepositorio celdaRepositorio;

    @Autowired
    private BarcoRepositorio barcoRepositorio;

    @Autowired
    private WebTestClient webTestClient;

    public PartidaControladorIntegrationTest(@Value("${server.port}") int serverPort) {
        this.SERVER_URL = "http://localhost:" + serverPort + "/";
    }

    private Long jugadorId;
    private Long mapaId;
    private Long barcoId;

    @BeforeEach
    void init(){
        Jugador j = jugadorRepositorio.save(new Jugador("JugadorPartida"));
        jugadorId = j.getId();

        Mapa m = mapaRepositorio.save(new Mapa(2,2));
        Celda c = new Celda("P", 0, 0);
        c.setMapa(m);
        celdaRepositorio.save(c);
        mapaId = m.getId();

        Barco b = new Barco("BarcoParaPartida", 0, 0, 0, 0);
        barcoRepositorio.save(b);
        barcoId = b.getId();
    }

    @Test
    void testCrearYObtenerPartida() {
        CrearPartidaRequest req = new CrearPartidaRequest();
        req.setJugadorId(jugadorId);
        req.setMapaId(mapaId);
        req.setBarcoId(barcoId);

        PartidaDTO creado = webTestClient.post().uri(SERVER_URL+"partida/crear")
            .bodyValue(req)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(PartidaDTO.class)
            .returnResult().getResponseBody();

        assertEquals("JugadorPartida", creado.getJugadorNombre());

        // obtener partida activa por jugador
        webTestClient.get().uri(SERVER_URL+"partida/activa/"+jugadorId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(PartidaDTO.class)
            .value(p -> assertEquals(creado.getId(), p.getId()));

        // obtener por id
        webTestClient.get().uri(SERVER_URL+"partida/"+creado.getId())
            .exchange()
            .expectStatus().isOk()
            .expectBody(PartidaDTO.class)
            .value(p -> assertEquals(jugadorId, p.getJugadorId()));
    }
}
