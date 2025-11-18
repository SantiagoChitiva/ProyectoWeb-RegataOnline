package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.ErrorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.JwtAuthenticationResponse;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.LoginDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.ModeloDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Modelo;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Role;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.User;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.ModeloRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.UserRepository;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration-testing")
public class ModeloControladorIntegrationTest {

    @Value("${base.url}")
    private String BASE_URL;

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModeloRepositorio modeloRepositorio;

    private JwtAuthenticationResponse login(String email, String password) {
        RequestEntity<LoginDTO> request = RequestEntity.post(BASE_URL + "/auth/login")
                .body(new LoginDTO(email, password));
        ResponseEntity<JwtAuthenticationResponse> jwtResponse = rest.exchange(request, JwtAuthenticationResponse.class);
        JwtAuthenticationResponse body = jwtResponse.getBody();
        assertNotNull(body, "La respuesta del login no puede ser null");
        return body;
    }

    @BeforeEach
    void init(){
        // crear usuarios
        User admin = new User("Admin", "admin@example.com", passwordEncoder.encode("adminpass"), Role.ADMINISTRADOR);
        User user = new User("User", "user@example.com", passwordEncoder.encode("userpass"), Role.JUGADOR);
        userRepository.save(admin);
        userRepository.save(user);

        modeloRepositorio.save(new Modelo("Modelo1", "azul"));
    }

    @Test
    void testGetModelos() throws Exception {
        JwtAuthenticationResponse adminLogin = login("admin@example.com", "adminpass");
        String token = adminLogin.getToken();

        RequestEntity<Void> req = RequestEntity.get(BASE_URL + "/modelo/list")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        ResponseEntity<String> resp = rest.exchange(req, String.class);
        assertTrue(resp.getStatusCode().is2xxSuccessful());
        String raw = resp.getBody();
        assertNotNull(raw);
        ObjectMapper mapper = new ObjectMapper();
        ModeloDTO[] arr = mapper.readValue(raw, ModeloDTO[].class);
        assertEquals(1, arr.length);
        assertEquals("Modelo1", arr[0].getNombreModelo());
    }

    @Test
    void testGetModelosPaginacionValida() {
        JwtAuthenticationResponse adminLogin = login("admin@example.com", "adminpass");
        String token = adminLogin.getToken();

        RequestEntity<Void> req = RequestEntity.get(BASE_URL + "/modelo/list/0")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        ResponseEntity<ModeloDTO[]> resp = rest.exchange(req, ModeloDTO[].class);
        assertTrue(resp.getStatusCode().is2xxSuccessful());
        assertNotNull(resp.getBody());
        assertEquals(1, resp.getBody().length);
    }

    @Test
    void testGetModelosPaginacionInvalida() {
        JwtAuthenticationResponse adminLogin = login("admin@example.com", "adminpass");
        String token = adminLogin.getToken();

        RequestEntity<Void> req = RequestEntity.get(BASE_URL + "/modelo/list/-1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        ResponseEntity<ErrorDTO> resp = rest.exchange(req, ErrorDTO.class);
        assertEquals(400, resp.getStatusCodeValue());
        assertNotNull(resp.getBody());
        assertEquals("El numero de pagina debe ser mayor o igual a 0", resp.getBody().getErrorString());
    }

    @Test
    void testBusquedaSinParamDevuelveLista() throws Exception {
        JwtAuthenticationResponse adminLogin = login("admin@example.com", "adminpass");
        String token = adminLogin.getToken();

        RequestEntity<Void> req = RequestEntity.get(BASE_URL + "/modelo/search")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        ResponseEntity<String> resp = rest.exchange(req, String.class);
        assertTrue(resp.getStatusCode().is2xxSuccessful());
        ObjectMapper mapper = new ObjectMapper();
        ModeloDTO[] arr = mapper.readValue(resp.getBody(), ModeloDTO[].class);
        assertEquals(1, arr.length);
    }

    @Test
    void testTextoBusquedaVacio() {
        JwtAuthenticationResponse adminLogin = login("admin@example.com", "adminpass");
        String token = adminLogin.getToken();

        RequestEntity<Void> req = RequestEntity.get(BASE_URL + "/modelo/search?searchText=   ")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        ResponseEntity<ErrorDTO> resp = rest.exchange(req, ErrorDTO.class);
        assertEquals(400, resp.getStatusCodeValue());
        assertNotNull(resp.getBody());
        assertEquals("El texto de búsqueda no puede estar vacío", resp.getBody().getErrorString());
    }

    @Test
    void modelosSinLogin() throws Exception {
        RequestEntity<Void> reqNoAuth = RequestEntity.get(BASE_URL + "/modelo/list").build();
        ResponseEntity<String> respNoAuth = rest.exchange(reqNoAuth, String.class);
        assertTrue(respNoAuth.getStatusCode().is4xxClientError());
    }

    @Test
    void testGetModeloPorId() throws Exception {
        JwtAuthenticationResponse adminLogin = login("admin@example.com", "adminpass");
        String token = adminLogin.getToken();

        Long id = modeloRepositorio.findAll().get(0).getId();
        RequestEntity<Void> req = RequestEntity.get(BASE_URL + "/modelo/" + id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        ResponseEntity<String> resp = rest.exchange(req, String.class);
        assertTrue(resp.getStatusCode().is2xxSuccessful());
        ObjectMapper mapper = new ObjectMapper();
        ModeloDTO dto = mapper.readValue(resp.getBody(), ModeloDTO.class);
        assertEquals("Modelo1", dto.getNombreModelo());
    }

    @Test
    void testCRUDModeloComoAdmin() {
        JwtAuthenticationResponse adminLogin = login("admin@example.com", "adminpass");
        String token = adminLogin.getToken();

        // Crear
        ModeloDTO nuevo = new ModeloDTO();
        nuevo.setNombreModelo("ModeloNuevo");
        nuevo.setColor("rojo");

        RequestEntity<ModeloDTO> createReq = RequestEntity.post(BASE_URL + "/modelo")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(nuevo);
        ResponseEntity<ModeloDTO> creadoResp = rest.exchange(createReq, ModeloDTO.class);
        assertTrue(creadoResp.getStatusCode().is2xxSuccessful());
        ModeloDTO creado = creadoResp.getBody();
        assertNotNull(creado);
        assertEquals("ModeloNuevo", creado.getNombreModelo());

        // Actualizar
        creado.setNombreModelo("ModeloActualizado");
        RequestEntity<ModeloDTO> putReq = RequestEntity.put(BASE_URL + "/modelo")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(creado);
        ResponseEntity<ModeloDTO> actualizadoResp = rest.exchange(putReq, ModeloDTO.class);
        assertTrue(actualizadoResp.getStatusCode().is2xxSuccessful());
        assertEquals("ModeloActualizado", actualizadoResp.getBody().getNombreModelo());

        // Borrar
        RequestEntity<Void> delReq = RequestEntity.delete(BASE_URL + "/modelo/" + creado.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        ResponseEntity<Void> delResp = rest.exchange(delReq, Void.class);
        assertTrue(delResp.getStatusCode().is2xxSuccessful());
        assertEquals(1, modeloRepositorio.findAll().size());
    }

    @Test
    void testCrearModeloComoUser_Forbidden() {
        JwtAuthenticationResponse userLogin = login("user@example.com", "userpass");
        String token = userLogin.getToken();
        ModeloDTO nuevo = new ModeloDTO();
        nuevo.setNombreModelo("ModeloNoPermitido");
        nuevo.setColor("negro");

        RequestEntity<ModeloDTO> createReq = RequestEntity.post(BASE_URL + "/modelo")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(nuevo);
        ResponseEntity<Void> creadoResp = rest.exchange(createReq, Void.class);
        assertTrue(creadoResp.getStatusCode().is4xxClientError());
    }
}
