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

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoModeloDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Barco;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Modelo;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.BarcoRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.ModeloRepositorio;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration-testing")
public class BarcoModeloControladorIntegrationTest {

    private String SERVER_URL;

    @Autowired
    private ModeloRepositorio modeloRepositorio;

    @Autowired
    private BarcoRepositorio barcoRepositorio;

    @Autowired
    private WebTestClient webTestClient;

    public BarcoModeloControladorIntegrationTest(@Value("${server.port}") int serverPort) {
        this.SERVER_URL = "http://localhost:" + serverPort + "/";
    }

    @BeforeEach
    void init() {
        Modelo m = new Modelo("Modelo1", "rojo");
        modeloRepositorio.save(m);

        Barco b = new Barco("Barco1", 0, 0, 0, 0);
        b.setModelo(m);
        barcoRepositorio.save(b);
    }

    @Test
    void testGetRelacionModeloExistente() {
        Modelo m = modeloRepositorio.findAll().get(0);
        Long modeloId = m.getId();

        webTestClient.get().uri(SERVER_URL + "barco/modelos/" + modeloId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(BarcoModeloDTO.class)
            .value(dto -> {
                assertEquals(modeloId, dto.getModeloId());
                assertEquals(1, dto.getBarcosIds().size());
            });
    }

    @Test
    void testGetRelacionModeloNoExistente() {
        webTestClient.get().uri(SERVER_URL + "barco/modelos/99999")
            .exchange()
            .expectStatus().isOk()
            .expectBody().consumeWith(res -> {
                byte[] body = res.getResponseBody();
                if (body != null && body.length > 0) {
                    String s = new String(body);
                    assertEquals("null", s.trim());
                }
            });
    }

    @Test
    void testActualizarModeloDeBarcosVacio() {
        Modelo m = modeloRepositorio.findAll().get(0);
        Long modeloId = m.getId();

        BarcoModeloDTO dto = new BarcoModeloDTO(modeloId, List.of());

        webTestClient.put().uri(SERVER_URL + "barco/modelos")
            .bodyValue(dto)
            .exchange()
            .expectStatus().isOk();

        long asociados = barcoRepositorio.findAll().stream()
            .filter(b -> b.getModelo() != null && b.getModelo().getId().equals(modeloId))
            .count();

        assertEquals(0, asociados);
    }

    @Test
    void testActualizarModeloDeBarcosAsignarBarco() {
        Modelo m = modeloRepositorio.findAll().get(0);
        Long modeloId = m.getId();

        // crear un barco sin modelo
        Barco otro = new Barco("Barco2", 0, 0, 0, 0);
        barcoRepositorio.save(otro);

        BarcoModeloDTO dto = new BarcoModeloDTO(modeloId, List.of(otro.getId()));

        webTestClient.put().uri(SERVER_URL + "barco/modelos")
            .bodyValue(dto)
            .exchange()
            .expectStatus().isOk();

        Barco actualizado = barcoRepositorio.findById(otro.getId()).orElseThrow();
        assertEquals(modeloId, actualizado.getModelo().getId());
    }
}
