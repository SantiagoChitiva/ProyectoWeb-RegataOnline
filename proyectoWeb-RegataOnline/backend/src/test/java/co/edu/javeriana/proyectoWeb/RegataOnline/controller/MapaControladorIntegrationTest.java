package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import java.util.ArrayList;
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

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.CeldaDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.CrearMapaRequest;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.MapaDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Mapa;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.MapaRepositorio;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration-testing")
public class MapaControladorIntegrationTest {

    private String SERVER_URL;

    @Autowired
    private MapaRepositorio mapaRepositorio;

    @Autowired
    private WebTestClient webTestClient;

    public MapaControladorIntegrationTest(@Value("${server.port}") int serverPort) {
        this.SERVER_URL = "http://localhost:" + serverPort + "/";
    }

    @BeforeEach
    void init(){
        // las pruebas crean mapas con controladores o repo
    }

    @Test
    void testCrearYListarMapa() {
        List<CeldaDTO> celdas = new ArrayList<>();
        CeldaDTO c = new CeldaDTO();
        c.setTipo("P");
        c.setPosicionX(0);
        c.setPosicionY(0);
        celdas.add(c);

        CrearMapaRequest req = new CrearMapaRequest(2, 2, celdas);

        MapaDTO creado = webTestClient.post().uri(SERVER_URL+"mapa/crear")
            .bodyValue(req)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(MapaDTO.class)
            .returnResult().getResponseBody();

        assertEquals(2, creado.getFilas());
        assertEquals(2, creado.getColumnas());

        // verificar listar
        webTestClient.get().uri(SERVER_URL+"mapa/list")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(MapaDTO.class)
            .hasSize(1);
    }

    @Test
    void testGetYBorrarMapa() {
        // crear con repo para obtener id
        Mapa m = mapaRepositorio.save(new Mapa(3,3));

        webTestClient.get().uri(SERVER_URL+"mapa/"+m.getId())
            .exchange()
            .expectStatus().isOk()
            .expectBody(MapaDTO.class)
            .value(dto -> assertEquals(3, dto.getFilas()));

        webTestClient.delete().uri(SERVER_URL+"mapa/"+m.getId())
            .exchange()
            .expectStatus().isOk();

        assertEquals(0, mapaRepositorio.findAll().size());
    }
}

