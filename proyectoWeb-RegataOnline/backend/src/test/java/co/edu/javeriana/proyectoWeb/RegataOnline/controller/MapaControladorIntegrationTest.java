package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import java.util.ArrayList;
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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.CeldaDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.CrearMapaRequest;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.JwtAuthenticationResponse;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.LoginDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.MapaDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Mapa;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Role;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.User;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.MapaRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.UserRepository;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration-testing")
public class MapaControladorIntegrationTest {

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
    }

    @Test
    void testCrearYListarMapa() throws Exception {
        JwtAuthenticationResponse adminLogin = login("admin@example.com", "adminpass");
        String token = adminLogin.getToken();

        List<CeldaDTO> celdas = new ArrayList<>();
        CeldaDTO c = new CeldaDTO();
        c.setTipo("P");
        c.setPosicionX(0);
        c.setPosicionY(0);
        celdas.add(c);

        CrearMapaRequest reqBody = new CrearMapaRequest(2, 2, celdas);

        RequestEntity<CrearMapaRequest> req = RequestEntity.post(BASE_URL + "/mapa/crear")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .body(reqBody);
        ResponseEntity<MapaDTO> creadoResp = rest.exchange(req, MapaDTO.class);
        assertTrue(creadoResp.getStatusCode().is2xxSuccessful() || creadoResp.getStatusCodeValue() == 201);
        MapaDTO creado = creadoResp.getBody();
        assertNotNull(creado);
        assertEquals(2, creado.getFilas());
        assertEquals(2, creado.getColumnas());

        // verificar listar (como admin)
        RequestEntity<Void> listReq = RequestEntity.get(BASE_URL + "/mapa/list")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        ResponseEntity<String> listResp = rest.exchange(listReq, String.class);
        assertTrue(listResp.getStatusCode().is2xxSuccessful());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MapaDTO[] arr = mapper.readValue(listResp.getBody(), MapaDTO[].class);
        assertEquals(1, arr.length);
    }

    @Test
    void testGetYBorrarMapa() {
        JwtAuthenticationResponse adminLogin = login("admin@example.com", "adminpass");
        String token = adminLogin.getToken();

        // crear con repo para obtener id
        Mapa m = mapaRepositorio.save(new Mapa(3,3));

        RequestEntity<Void> getReq = RequestEntity.get(BASE_URL + "/mapa/" + m.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        ResponseEntity<MapaDTO> getResp = rest.exchange(getReq, MapaDTO.class);
        assertTrue(getResp.getStatusCode().is2xxSuccessful());
        MapaDTO dto = getResp.getBody();
        assertNotNull(dto);
        assertEquals(3, dto.getFilas());

        RequestEntity<Void> delReq = RequestEntity.delete(BASE_URL + "/mapa/" + m.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        ResponseEntity<Void> delResp = rest.exchange(delReq, Void.class);
        assertTrue(delResp.getStatusCode().is2xxSuccessful());

        assertEquals(0, mapaRepositorio.findAll().size());
    }

    @Test
    void crearMapaSinLogin() {
        List<CeldaDTO> celdas = new ArrayList<>();
        CeldaDTO c = new CeldaDTO();
        c.setTipo("P");
        c.setPosicionX(0);
        c.setPosicionY(0);
        celdas.add(c);

        CrearMapaRequest reqBody = new CrearMapaRequest(2, 2, celdas);

        RequestEntity<CrearMapaRequest> req = RequestEntity.post(BASE_URL + "/mapa/crear")
            .body(reqBody);
        ResponseEntity<String> creadoResp = rest.exchange(req, String.class);
        assertTrue(creadoResp.getStatusCode().is4xxClientError());
    }

    @Test
    void crearMapaUsuarioNoAutorizado() {
        JwtAuthenticationResponse userLogin = login("user@example.com", "userpass");
        String token = userLogin.getToken();

        List<CeldaDTO> celdas = new ArrayList<>();
        CeldaDTO c = new CeldaDTO();
        c.setTipo("P");
        c.setPosicionX(0);
        c.setPosicionY(0);
        celdas.add(c);

        CrearMapaRequest reqBody = new CrearMapaRequest(2, 2, celdas);

        RequestEntity<CrearMapaRequest> req = RequestEntity.post(BASE_URL + "/mapa/crear")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .body(reqBody);
        ResponseEntity<String> creadoResp = rest.exchange(req, String.class);
        assertTrue(creadoResp.getStatusCode().is4xxClientError());
    }
}

