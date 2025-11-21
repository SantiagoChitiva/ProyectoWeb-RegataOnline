package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import java.net.URI;
import java.util.List;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;     
import org.springframework.web.util.UriComponentsBuilder;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.ErrorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.JwtAuthenticationResponse;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.LoginDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Barco;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Role;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.User;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.BarcoRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.UserRepository;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration-testing")
public class BarcoControladorIntegrationTest {

    @Value("${base.url}")
    private String BASE_URL;

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BarcoRepositorio barcoRepositorio;

    private JwtAuthenticationResponse login(String email, String password) {
        RequestEntity<LoginDTO> request = RequestEntity.post(BASE_URL + "/auth/login")
                .body(new LoginDTO(email, password));
        ResponseEntity<JwtAuthenticationResponse> jwtResponse = rest.exchange(request, JwtAuthenticationResponse.class);
        JwtAuthenticationResponse body = jwtResponse.getBody();
        assertNotNull(body);
        return body;
    }

    @BeforeEach
    void init(){
        // crear usuarios 
        User admin = new User("Admin", "admin@example.com", passwordEncoder.encode("adminpass"), Role.ADMINISTRADOR);
        User user = new User("User", "user@example.com", passwordEncoder.encode("userpass"), Role.JUGADOR);
        userRepository.save(admin);
        userRepository.save(user);

        barcoRepositorio.save(new Barco("Barco1", 0, 0, 0, 0));
    }

    @Test
    void testGetBarcos(){
        // sin token (sin autorización)
        RequestEntity<Void> reqNoAuth = RequestEntity.get(BASE_URL + "/barco/list").build();
        ResponseEntity<BarcoDTO[]> respNoAuth = rest.exchange(reqNoAuth, BarcoDTO[].class);
        assertTrue(respNoAuth.getStatusCode().is4xxClientError());

        // con token válido
        JwtAuthenticationResponse login = login("admin@example.com", "adminpass");
        assertNotNull(login, "respuesta del login no puede ser null");
        assertNotNull(login.getToken(), "token del login no puede ser null");
        String token = login.getToken();
        RequestEntity<Void> req = RequestEntity.get(BASE_URL + "/barco/list")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .build();
        ResponseEntity<BarcoDTO[]> resp = rest.exchange(req, BarcoDTO[].class);
        assertTrue(resp.getStatusCode().is2xxSuccessful());
        BarcoDTO[] body = resp.getBody();
        assertNotNull(body, "cuerpo de la respuesta no puede ser null");
        assertEquals(1, body.length);
        assertEquals("Barco1", body[0].getNombre());
    }

    @Test
    void testGetBarcosPaginacionValida() {
        JwtAuthenticationResponse login = login("admin@example.com", "adminpass");
        assertNotNull(login);
        assertNotNull(login.getToken());
        String token = login.getToken();
        RequestEntity<Void> req = RequestEntity.get(BASE_URL + "/barco/list/0")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .build();
        ResponseEntity<BarcoDTO[]> resp = rest.exchange(req, BarcoDTO[].class);
        assertTrue(resp.getStatusCode().is2xxSuccessful());
        assertNotNull(resp.getBody(), "cuerpo de la respuesta no puede ser null");
        assertEquals(1, resp.getBody().length);
    }

    @Test
    void testGetBarcosPaginacionInvalida() {
        JwtAuthenticationResponse login = login("admin@example.com", "adminpass");
        assertNotNull(login);
        assertNotNull(login.getToken());
        String token = login.getToken();
        RequestEntity<Void> req = RequestEntity.get(BASE_URL + "/barco/list/-1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .build();
        ResponseEntity<ErrorDTO> resp = rest.exchange(req, ErrorDTO.class);
        assertEquals(400, resp.getStatusCodeValue());
        assertNotNull(resp.getBody(), "cuerpo no puede ser null");
        assertEquals("El numero de pagina debe ser mayor o igual 0", resp.getBody().getErrorString());
    }

    @Test
    void testBusquedaSinParamDevuelveLista() {
        JwtAuthenticationResponse login = login("admin@example.com", "adminpass");
        assertNotNull(login);
        assertNotNull(login.getToken());
        String token = login.getToken();
        RequestEntity<Void> req = RequestEntity.get(BASE_URL + "/barco/search")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .build();
        ResponseEntity<BarcoDTO[]> resp = rest.exchange(req, BarcoDTO[].class);
        assertTrue(resp.getStatusCode().is2xxSuccessful());
        assertNotNull(resp.getBody(), "cuerpo de la respuesta no puede ser null");
        assertEquals(1, resp.getBody().length);
    }

    @Test
    void testTextoBusquedaVacio() {
        JwtAuthenticationResponse login = login("admin@example.com", "adminpass");
        String token = login.getToken();
        // encode query parameter to avoid illegal characters
        URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URL)
            .path("/barco/search")
            .queryParam("searchText", "   ")
            .build()
            .encode()
            .toUri();
        RequestEntity<Void> req = RequestEntity.get(uri)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .build();
        ResponseEntity<ErrorDTO> resp = rest.exchange(req, ErrorDTO.class);
        assertEquals(400, resp.getStatusCodeValue());
        assertNotNull(resp.getBody(), "cuerpo no puede ser null");
        assertEquals("El texto de búsqueda no puede estar vacío", resp.getBody().getErrorString());
    }

    @Test
    void testGetBarcoPorId() {
        Long id = barcoRepositorio.findAll().get(0).getId();
        JwtAuthenticationResponse login = login("user@example.com", "userpass");
        String token = login.getToken();
        RequestEntity<Void> req = RequestEntity.get(BASE_URL + "/barco/" + id)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .build();
        ResponseEntity<BarcoDTO> resp = rest.exchange(req, BarcoDTO.class);
        assertTrue(resp.getStatusCode().is2xxSuccessful());
        BarcoDTO barcoResp = resp.getBody();
        assertNotNull(barcoResp, "cuerpo de la respuesta no puede ser null");
        assertEquals("Barco1", barcoResp.getNombre());
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

        JwtAuthenticationResponse adminLogin = login("admin@example.com", "adminpass");
        String adminTokenLocal = adminLogin.getToken();
        RequestEntity<BarcoDTO> createReq = RequestEntity.post(BASE_URL + "/barco")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminTokenLocal)
            .body(nuevo);
        ResponseEntity<BarcoDTO> creadoResp = rest.exchange(createReq, BarcoDTO.class);
        org.junit.jupiter.api.Assertions.assertTrue(creadoResp.getStatusCode().is2xxSuccessful());
        BarcoDTO creado = creadoResp.getBody();
        assertNotNull(creado, "barco creado no puede ser null");
        // verificar creación
        List<?> all = barcoRepositorio.findAll();
        assertEquals(2, all.size());
        assertEquals("BarcoNuevo", creado.getNombre());

        // Actualizar
        creado.setNombre("BarcoActualizado");
        RequestEntity<BarcoDTO> updateReq = RequestEntity.put(BASE_URL + "/barco")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminTokenLocal)
            .body(creado);
        ResponseEntity<BarcoDTO> actualizadoResp = rest.exchange(updateReq, BarcoDTO.class);
        BarcoDTO actualizado = actualizadoResp.getBody();
        assertNotNull(actualizado, "barco actualizado no puede ser null");
        assertEquals("BarcoActualizado", actualizado.getNombre());

        // Borrar
            RequestEntity<Void> delReq = RequestEntity.delete(BASE_URL + "/barco/" + actualizado.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminTokenLocal)
                .build();
            ResponseEntity<Void> del = rest.exchange(delReq, Void.class);
            assertTrue(del.getStatusCode().is2xxSuccessful());

        assertEquals(1, barcoRepositorio.findAll().size());
    }

    @Test
    void testUserData() {
        JwtAuthenticationResponse admin = login("admin@example.com", "adminpass");
        JwtAuthenticationResponse user = login("user@example.com", "userpass");

        assertEquals("admin@example.com", admin.getEmail());
        assertEquals("user@example.com", user.getEmail());
    }

    @Test
    void crearBarcoSinLogin() {
        BarcoDTO nuevo = new BarcoDTO();
        nuevo.setNombre("BarcoNoAuth");
        nuevo.setVelocidadX(1);
        nuevo.setVelocidadY(1);
        nuevo.setPosicionX(0);
        nuevo.setPosicionY(0);

        RequestEntity<BarcoDTO> req = RequestEntity.post(BASE_URL + "/barco")
            .body(nuevo);
        ResponseEntity<Void> resp = rest.exchange(req, Void.class);
        assertTrue(resp.getStatusCode().is4xxClientError());
    }

    @Test
    void crearBarcoUsuarioNoAutorizado() {
        JwtAuthenticationResponse userLogin = login("user@example.com", "userpass");
        String token = userLogin.getToken();

        BarcoDTO nuevo = new BarcoDTO();
        nuevo.setNombre("BarcoUser");
        nuevo.setVelocidadX(1);
        nuevo.setVelocidadY(1);
        nuevo.setPosicionX(0);
        nuevo.setPosicionY(0);

        RequestEntity<BarcoDTO> req = RequestEntity.post(BASE_URL + "/barco")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .body(nuevo);
        ResponseEntity<Void> resp = rest.exchange(req, Void.class);
        assertEquals(HttpStatus.FORBIDDEN, resp.getStatusCode());
    }
}
