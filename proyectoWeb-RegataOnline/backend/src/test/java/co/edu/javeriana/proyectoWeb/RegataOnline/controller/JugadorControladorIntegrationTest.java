package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import java.util.List;

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

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.JugadorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Jugador;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.JugadorRepositorio;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration-testing")
public class JugadorControladorIntegrationTest {

    private String SERVER_URL;

    @Autowired
    private JugadorRepositorio jugadorRepositorio;

    @Autowired
    private WebTestClient webTestClient;

    public JugadorControladorIntegrationTest(@Value("${server.port}") int serverPort) {
        this.SERVER_URL = "http://localhost:" + serverPort + "/";
    }

    @BeforeEach
    void init(){
        jugadorRepositorio.save(new Jugador("Jugador1"));
    }

    @Test
    void testGetJugadores() {
        webTestClient.get().uri(SERVER_URL+"jugador/list")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(JugadorDTO.class)
            .hasSize(1)
            .value(list -> assertEquals("Jugador1", list.get(0).getNombre()));
    }

    @Test
    void testGetJugadorPorId() {
        Long id = jugadorRepositorio.findAll().get(0).getId();
        webTestClient.get().uri(SERVER_URL+"jugador/"+id)
            .exchange()
            .expectStatus().isOk()
            .expectBody(JugadorDTO.class)
            .value(j -> assertEquals("Jugador1", j.getNombre()));
    }

    @Test
    void testCRUDJugador() {
        // Crear
        JugadorDTO nuevo = new JugadorDTO();
        nuevo.setNombre("JugadorNuevo");

        webTestClient.post().uri(SERVER_URL+"jugador")
            .bodyValue(nuevo)
            .exchange()
            .expectStatus().isOk();

        // verificar creación
        List<?> all = jugadorRepositorio.findAll();
        assertEquals(2, all.size());

        // Actualizar
        Jugador existente = jugadorRepositorio.findAll().get(1);
        JugadorDTO actualizadoDTO = new JugadorDTO();
        actualizadoDTO.setId(existente.getId());
        actualizadoDTO.setNombre("JugadorActualizado");

        webTestClient.put().uri(SERVER_URL+"jugador")
            .bodyValue(actualizadoDTO)
            .exchange()
            .expectStatus().isOk();

        assertEquals("JugadorActualizado", jugadorRepositorio.findById(existente.getId()).get().getNombre());

        // Borrar
        webTestClient.delete().uri(SERVER_URL+"jugador/"+existente.getId())
            .exchange()
            .expectStatus().isOk();

        assertEquals(1, jugadorRepositorio.findAll().size());
    }
}
