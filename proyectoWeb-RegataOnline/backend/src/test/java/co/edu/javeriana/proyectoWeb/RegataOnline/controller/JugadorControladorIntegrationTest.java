package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

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

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.JugadorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.JwtAuthenticationResponse;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.LoginDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Jugador;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Role;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.User;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.JugadorRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.UserRepository;


@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration-testing")
public class JugadorControladorIntegrationTest {

    @Value("${base.url}")
    private String BASE_URL;

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JugadorRepositorio jugadorRepositorio;

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
        jugadorRepositorio.save(new Jugador("Jugador1"));
    }

    @Test
    void testGetJugadores() {
        // este endpoint requiere ADMIN
        RequestEntity<Void> reqNoAuth = RequestEntity.get(BASE_URL + "/jugador/list").build();
        ResponseEntity<String> respNoAuth = rest.exchange(reqNoAuth, String.class);
        assertTrue(respNoAuth.getStatusCode().is4xxClientError());

        JwtAuthenticationResponse adminLogin = login("admin@example.com", "adminpass");
        String token = adminLogin.getToken();
        RequestEntity<Void> req = RequestEntity.get(BASE_URL + "/jugador/list")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .build();
        ResponseEntity<JugadorDTO[]> resp = rest.exchange(req, JugadorDTO[].class);
        assertTrue(resp.getStatusCode().is2xxSuccessful());
        assertNotNull(resp.getBody(), "El cuerpo de la respuesta no puede ser null");
        assertEquals(1, resp.getBody().length);
        assertEquals("Jugador1", resp.getBody()[0].getNombre());
    }

    @Test
    void testGetJugadorPorId() {
        JwtAuthenticationResponse adminLogin = login("admin@example.com", "adminpass");
        String token = adminLogin.getToken();
        Long id = jugadorRepositorio.findAll().get(0).getId();
        RequestEntity<Void> req = RequestEntity.get(BASE_URL + "/jugador/"+id)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .build();
        ResponseEntity<JugadorDTO> resp = rest.exchange(req, JugadorDTO.class);
        assertTrue(resp.getStatusCode().is2xxSuccessful());
        JugadorDTO dto = resp.getBody();
        assertNotNull(dto, "El DTO de respuesta no puede ser null");
        assertEquals("Jugador1", dto.getNombre());
    }

    @Test
    void testCRUDJugador() {
        JwtAuthenticationResponse adminLogin = login("admin@example.com", "adminpass");
        String token = adminLogin.getToken();
        // Crear (admin)
        JugadorDTO nuevo = new JugadorDTO();
        nuevo.setNombre("JugadorNuevo");

        RequestEntity<JugadorDTO> createReq = RequestEntity.post(BASE_URL + "/jugador")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .body(nuevo);
        ResponseEntity<Void> createResp = rest.exchange(createReq, Void.class);
        assertTrue(createResp.getStatusCode().is2xxSuccessful());

        List<?> all = jugadorRepositorio.findAll();
        assertEquals(2, all.size());

        // Actualizar
        Jugador existente = jugadorRepositorio.findAll().get(1);
        JugadorDTO actualizadoDTO = new JugadorDTO();
        actualizadoDTO.setId(existente.getId());
        actualizadoDTO.setNombre("JugadorActualizado");

        RequestEntity<JugadorDTO> putReq = RequestEntity.put(BASE_URL + "/jugador")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .body(actualizadoDTO);
        ResponseEntity<Void> putResp = rest.exchange(putReq, Void.class);
        assertTrue(putResp.getStatusCode().is2xxSuccessful());

        assertEquals("JugadorActualizado", jugadorRepositorio.findById(existente.getId()).get().getNombre());

        // Borrar
        RequestEntity<Void> delReq = RequestEntity.delete(BASE_URL + "/jugador/"+existente.getId())
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .build();
        ResponseEntity<Void> delResp = rest.exchange(delReq, Void.class);
        assertTrue(delResp.getStatusCode().is2xxSuccessful());

        assertEquals(1, jugadorRepositorio.findAll().size());
    }

    @Test
    void jugadoresUsuarioNoAutorizado() {
        JwtAuthenticationResponse userLogin = login("user@example.com", "userpass");
        String token = userLogin.getToken();

        RequestEntity<Void> req = RequestEntity.get(BASE_URL + "/jugador/list")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .build();
        ResponseEntity<String> resp = rest.exchange(req, String.class);
        assertEquals(HttpStatus.FORBIDDEN, resp.getStatusCode());
    }
}
