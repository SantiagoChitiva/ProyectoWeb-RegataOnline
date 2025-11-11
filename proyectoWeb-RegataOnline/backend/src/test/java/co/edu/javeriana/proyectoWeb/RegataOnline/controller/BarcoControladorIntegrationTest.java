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

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.ErrorDTO;
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
            .expectBodyList(BarcoDTO.class)
            .hasSize(1)
            .value(list -> assertEquals("Barco1", list.get(0).getNombre()));
    }

    @Test
    void testGetBarcosPaginacionValida() {
        webTestClient.get().uri(SERVER_URL+"barco/list/0")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(BarcoDTO.class)
            .hasSize(1);
    }

    @Test
    void testGetBarcosPaginacionInvalida() {
        webTestClient.get().uri(SERVER_URL+"barco/list/-1")
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorDTO.class)
            .value(err -> assertEquals("El numero de pagina debe ser mayor o igual 0", err.getErrorString()));
    }

    @Test
    void testBusquedaSinParamDevuelveLista() {
        webTestClient.get().uri(SERVER_URL+"barco/search")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(BarcoDTO.class)
            .hasSize(1);
    }

    @Test
    void testTextoBusquedaVacio() {
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/barco/search").queryParam("searchText", "   ").build())
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorDTO.class)
            .value(err -> assertEquals("El texto de búsqueda no puede estar vacío", err.getErrorString()));
    }

    @Test
    void testGetBarcoPorId() {
        Long id = barcoRepositorio.findAll().get(0).getId();
        webTestClient.get().uri(SERVER_URL+"barco/"+id)
            .exchange()
            .expectStatus().isOk()
            .expectBody(BarcoDTO.class)
            .value(b -> assertEquals("Barco1", b.getNombre()));
    }

    @Test
    void testCRUDBarco() {
        // Crear
        BarcoDTO nuevo = new BarcoDTO();
        nuevo.setNombre("BarcoNuevo");
        nuevo.setVelocidadX(1);
        nuevo.setVelocidadY(2);
        nuevo.setPosicionX(3);
        nuevo.setPosicionY(4);

        BarcoDTO creado = webTestClient.post().uri(SERVER_URL+"barco")
            .bodyValue(nuevo)
            .exchange()
            .expectStatus().isOk()
            .expectBody(BarcoDTO.class)
            .returnResult().getResponseBody();

        // verificar creación
        List<?> all = barcoRepositorio.findAll();
        assertEquals(2, all.size());
        assertEquals("BarcoNuevo", creado.getNombre());

        // Actualizar
        creado.setNombre("BarcoActualizado");
        BarcoDTO actualizado = webTestClient.put().uri(SERVER_URL+"barco")
            .bodyValue(creado)
            .exchange()
            .expectStatus().isOk()
            .expectBody(BarcoDTO.class)
            .returnResult().getResponseBody();

        assertEquals("BarcoActualizado", actualizado.getNombre());

        // Borrar
        webTestClient.delete().uri(SERVER_URL+"barco/"+actualizado.getId())
            .exchange()
            .expectStatus().isOk();

        assertEquals(1, barcoRepositorio.findAll().size());
    }
}
