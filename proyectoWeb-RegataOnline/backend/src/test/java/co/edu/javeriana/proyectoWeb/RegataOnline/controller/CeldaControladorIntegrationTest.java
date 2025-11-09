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

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.CeldaDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Celda;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Mapa;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.CeldaRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.MapaRepositorio;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration-testing")
public class CeldaControladorIntegrationTest {

    private String SERVER_URL;

    @Autowired
    private CeldaRepositorio celdaRepositorio;

    @Autowired
    private MapaRepositorio mapaRepositorio;

    @Autowired
    private WebTestClient webTestClient;

    public CeldaControladorIntegrationTest(@Value("${server.port}") int serverPort) {
        this.SERVER_URL = "http://localhost:" + serverPort + "/";
    }

    @BeforeEach
    void init(){
        Mapa mapa = mapaRepositorio.save(new Mapa(2,2));
        Celda c = new Celda("", 0, 0);
        c.setMapa(mapa);
        celdaRepositorio.save(c);
    }

    @Test
    void testGetCeldas() {
        webTestClient.get().uri(SERVER_URL+"celda/list")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(CeldaDTO.class)
            .hasSize(1)
            .value(list -> assertEquals(0, list.get(0).getPosicionX()));
    }

    @Test
    void testGetCeldaPorId() {
        Long id = celdaRepositorio.findAll().get(0).getId();
        webTestClient.get().uri(SERVER_URL+"celda/"+id)
            .exchange()
            .expectStatus().isOk()
            .expectBody(CeldaDTO.class)
            .value(c -> assertEquals(0, c.getPosicionX()));
    }
}

