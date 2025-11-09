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

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.ErrorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.ModeloDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Modelo;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.ModeloRepositorio;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration-testing")
public class ModeloControladorIntegrationTest {

    private String SERVER_URL;

    @Autowired
    private ModeloRepositorio modeloRepositorio;

    @Autowired
    private WebTestClient webTestClient;

    public ModeloControladorIntegrationTest(@Value("${server.port}") int serverPort) {
        this.SERVER_URL = "http://localhost:" + serverPort + "/";
    }

    @BeforeEach
    void init(){
        modeloRepositorio.save(new Modelo("Modelo1", "azul"));
    }

    @Test
    void testGetModelos() {
        webTestClient.get().uri(SERVER_URL+"modelo/list")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(ModeloDTO.class)
            .hasSize(1)
            .value(list -> assertEquals("Modelo1", list.get(0).getNombreModelo()));
    }

    @Test
    void testGetModelosPaginacionValida() {
        webTestClient.get().uri(SERVER_URL+"modelo/list/0")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(ModeloDTO.class)
            .hasSize(1);
    }

    @Test
    void testGetModelosPaginacionInvalida() {
        webTestClient.get().uri(SERVER_URL+"modelo/list/-1")
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorDTO.class)
            .value(err -> assertEquals("El numero de pagina debe ser mayor o igual a 0", err.getErrorString()));
    }

    @Test
    void testBusquedaSinParamDevuelveLista() {
        webTestClient.get().uri(SERVER_URL+"modelo/search")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(ModeloDTO.class)
            .hasSize(1);
    }

    @Test
    void testTextoBusquedaVacio() {
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/modelo/search").queryParam("searchText", "   ").build())
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorDTO.class)
            .value(err -> assertEquals("El texto de búsqueda no puede estar vacío", err.getErrorString()));
    }

    @Test
    void testGetModeloPorId() {
        Long id = modeloRepositorio.findAll().get(0).getId();
        webTestClient.get().uri(SERVER_URL+"modelo/"+id)
            .exchange()
            .expectStatus().isOk()
            .expectBody(ModeloDTO.class)
            .value(m -> assertEquals("Modelo1", m.getNombreModelo()));
    }

    @Test
    void testCRUDModelo() {
        // Crear
        ModeloDTO nuevo = new ModeloDTO();
        nuevo.setNombreModelo("ModeloNuevo");
        nuevo.setColor("rojo");

        ModeloDTO creado = webTestClient.post().uri(SERVER_URL+"modelo")
            .bodyValue(nuevo)
            .exchange()
            .expectStatus().isOk()
            .expectBody(ModeloDTO.class)
            .returnResult().getResponseBody();

        // verificar creación
        assertEquals("ModeloNuevo", creado.getNombreModelo());
        assertEquals(2, modeloRepositorio.findAll().size());

        // Actualizar
        creado.setNombreModelo("ModeloActualizado");
        ModeloDTO actualizado = webTestClient.put().uri(SERVER_URL+"modelo")
            .bodyValue(creado)
            .exchange()
            .expectStatus().isOk()
            .expectBody(ModeloDTO.class)
            .returnResult().getResponseBody();

        assertEquals("ModeloActualizado", actualizado.getNombreModelo());

        // Borrar
        webTestClient.delete().uri(SERVER_URL+"modelo/"+actualizado.getId())
            .exchange()
            .expectStatus().isOk();

        assertEquals(1, modeloRepositorio.findAll().size());
    }
}
