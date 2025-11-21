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
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.CeldaDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.JwtAuthenticationResponse;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.LoginDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Celda;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Mapa;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Role;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.User;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.CeldaRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.MapaRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.UserRepository;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration-testing")
public class CeldaControladorIntegrationTest {

    @Value("${base.url}")
    private String BASE_URL;

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MapaRepositorio mapaRepositorio;

    @Autowired
    private CeldaRepositorio celdaRepositorio;

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

        Mapa mapa = mapaRepositorio.save(new Mapa(2,2));
        Celda c = new Celda("", 0, 0);
        c.setMapa(mapa);
        celdaRepositorio.save(c);
    }

    @Test
    void testGetCeldas() throws Exception {
        // sin token -> 4xx
        RequestEntity<Void> reqNoAuth = RequestEntity.get(BASE_URL + "/celda/list").build();
        ResponseEntity<String> respNoAuth = rest.exchange(reqNoAuth, String.class);
        assertTrue(respNoAuth.getStatusCode().is4xxClientError());

        JwtAuthenticationResponse adminLogin = login("admin@example.com", "adminpass");
        String token = adminLogin.getToken();

        RequestEntity<Void> req = RequestEntity.get(BASE_URL + "/celda/list")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        ResponseEntity<String> resp = rest.exchange(req, String.class);
        assertTrue(resp.getStatusCode().is2xxSuccessful());
        String raw = resp.getBody();
        assertNotNull(raw, "El cuerpo de la respuesta no puede ser null");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CeldaDTO[] arr = mapper.readValue(raw, CeldaDTO[].class);
        assertEquals(1, arr.length);
        assertEquals(0, arr[0].getPosicionX());
    }

    @Test
    void testGetCeldaPorId() throws Exception {
        JwtAuthenticationResponse adminLogin = login("admin@example.com", "adminpass");
        String token = adminLogin.getToken();

        Long id = celdaRepositorio.findAll().get(0).getId();
        RequestEntity<Void> req = RequestEntity.get(BASE_URL + "/celda/" + id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        ResponseEntity<String> resp = rest.exchange(req, String.class);
        assertTrue(resp.getStatusCode().is2xxSuccessful());
        String raw = resp.getBody();
        assertNotNull(raw);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CeldaDTO dto = mapper.readValue(raw, CeldaDTO.class);
        assertEquals(0, dto.getPosicionX());
    }

    @Test
    void celdasUsuarioNoAutorizado() {
        JwtAuthenticationResponse userLogin = login("user@example.com", "userpass");
        String token = userLogin.getToken();

        RequestEntity<Void> req = RequestEntity.get(BASE_URL + "/celda/list")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .build();
        ResponseEntity<String> resp = rest.exchange(req, String.class);
        assertEquals(HttpStatus.FORBIDDEN, resp.getStatusCode());
    }
}
