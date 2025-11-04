package co.edu.javeriana.proyectoWeb.RegataOnline.controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import co.edu.javeriana.proyectoWeb.RegataOnline.model.Barco;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.BarcoRepositorio;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration-testing")
public class BarcoControladorIntegrationTest {

    private String SERVER_URL;

    @Autowired
    private BarcoRepositorio barcoRepositorio;

    @Autowired
    private WebTestClient webTestClient;

    public BarcoControladorIntegrationTest(@Value("${server.port}") int serverPort) {
        this.SERVER_URL = "http://localhost:" + serverPort + "/";
    }

    @BeforeEach
    void init(){
        barcoRepositorio.save(new Barco("Barco1", 0, 0, 0, 0));
    }

    @Test
    void testGetBarcos() {
        webTestClient.get().uri(SERVER_URL+"barco/list")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(Barco.class)
            .hasSize(1)
            .contains(new Barco("Barco1", 0, 0, 0, 0));
    }
}
