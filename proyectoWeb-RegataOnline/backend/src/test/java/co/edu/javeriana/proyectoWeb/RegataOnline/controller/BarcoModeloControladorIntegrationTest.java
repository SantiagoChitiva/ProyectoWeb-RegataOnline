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
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoModeloDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.JwtAuthenticationResponse;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.LoginDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Barco;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Modelo;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Role;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.User;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.BarcoRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.ModeloRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.UserRepository;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration-testing")
public class BarcoModeloControladorIntegrationTest {

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

    @Autowired
    private BarcoRepositorio barcoRepositorio;

    private JwtAuthenticationResponse login(String email, String password) {
        RequestEntity<LoginDTO> request = RequestEntity.post(BASE_URL + "/auth/login")
                .body(new LoginDTO(email, password));
        ResponseEntity<JwtAuthenticationResponse> jwtResponse = rest.exchange(request, JwtAuthenticationResponse.class);
        JwtAuthenticationResponse body = jwtResponse.getBody();
        assertNotNull(body, "La respuesta del login no puede ser null");
        return body;
    }

    @BeforeEach
    void init() {
        // crear usuarios
        User admin = new User("Admin", "admin@example.com", passwordEncoder.encode("adminpass"), Role.ADMINISTRADOR);
        User user = new User("User", "user@example.com", passwordEncoder.encode("userpass"), Role.JUGADOR);
        userRepository.save(admin);
        userRepository.save(user);

        Modelo m = new Modelo("Modelo1", "rojo");
        modeloRepositorio.save(m);

        Barco b = new Barco("Barco1", 0, 0, 0, 0);
        b.setModelo(m);
        barcoRepositorio.save(b);
    }

    @Test
    void testGetRelacionModeloExistente() throws Exception {
        // este endpoint está protegido a ADMINISTRADOR 
        JwtAuthenticationResponse adminLogin = login("admin@example.com", "adminpass");
        String token = adminLogin.getToken();

        Modelo m = modeloRepositorio.findAll().get(0);
        Long modeloId = m.getId();

        RequestEntity<Void> req = RequestEntity.get(BASE_URL + "/barco/modelos/" + modeloId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        ResponseEntity<String> resp = rest.exchange(req, String.class);
        assertTrue(resp.getStatusCode().is2xxSuccessful(), "La respuesta debe ser exitosa");
        String rawBody = resp.getBody();
        assertNotNull(rawBody, "El cuerpo de la respuesta no puede ser null");
        ObjectMapper mapper = new ObjectMapper();
        BarcoModeloDTO dto = mapper.readValue(rawBody, BarcoModeloDTO.class);
        assertNotNull(dto, "El DTO de respuesta no puede ser null");
        assertEquals(modeloId, dto.getModeloId());
        assertEquals(1, dto.getBarcosIds().size());
    }

    @Test
    void testGetRelacionModeloNoExistente() throws Exception {
        // este endpoint está protegido a ADMINISTRADOR 
        JwtAuthenticationResponse adminLogin = login("admin@example.com", "adminpass");
        String token = adminLogin.getToken();

        RequestEntity<Void> req = RequestEntity.get(BASE_URL + "/barco/modelos/99999")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        ResponseEntity<String> resp = rest.exchange(req, String.class);
        assertTrue(resp.getStatusCode().is2xxSuccessful(), "La respuesta debe ser exitosa");
        String raw = resp.getBody();
        if (raw != null) assertEquals("null", raw.trim());
    }

    @Test
    void testGetRelacionModeloSinAutorizacion() {
        // intento sin token
        RequestEntity<Void> reqNoAuth = RequestEntity.get(BASE_URL + "/barco/modelos/1").build();
        ResponseEntity<String> respNoAuth = rest.exchange(reqNoAuth, String.class);
        assertTrue(respNoAuth.getStatusCode().is4xxClientError(), "Sin token debe devolver 4xx");
    }

    @Test
    void testActualizarModeloDeBarcosVacioComoAdmin() {
        JwtAuthenticationResponse adminLogin = login("admin@example.com", "adminpass");
        String token = adminLogin.getToken();

        Modelo m = modeloRepositorio.findAll().get(0);
        Long modeloId = m.getId();

        BarcoModeloDTO dto = new BarcoModeloDTO(modeloId, List.of());

        RequestEntity<BarcoModeloDTO> req = RequestEntity.put(BASE_URL + "/barco/modelos")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(dto);
        ResponseEntity<Void> resp = rest.exchange(req, Void.class);
        assertTrue(resp.getStatusCode().is2xxSuccessful());

        long asociados = barcoRepositorio.findAll().stream()
                .filter(b -> b.getModelo() != null && b.getModelo().getId().equals(modeloId))
                .count();

        assertEquals(0, asociados);
    }

    @Test
    void testActualizarModeloDeBarcosComoUser_Forbidden() {
        JwtAuthenticationResponse userLogin = login("user@example.com", "userpass");
        String token = userLogin.getToken();

        Modelo m = modeloRepositorio.findAll().get(0);
        Long modeloId = m.getId();

        // crear un barco sin modelo
        Barco otro = new Barco("Barco2", 0, 0, 0, 0);
        barcoRepositorio.save(otro);

        BarcoModeloDTO dto = new BarcoModeloDTO(modeloId, List.of(otro.getId()));

        RequestEntity<BarcoModeloDTO> req = RequestEntity.put(BASE_URL + "/barco/modelos")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(dto);
        ResponseEntity<Void> resp = rest.exchange(req, Void.class);
        // el usuario sin rol admin no debe poder modificar modelos
        assertTrue(resp.getStatusCode().is4xxClientError());
    }
}
