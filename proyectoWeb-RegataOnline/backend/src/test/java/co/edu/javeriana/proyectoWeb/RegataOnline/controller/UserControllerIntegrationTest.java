package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import java.util.Map;

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

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.JwtAuthenticationResponse;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.LoginDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Role;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.User;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.UserRepository;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration-testing")
public class UserControllerIntegrationTest {

    @Value("${base.url}")
    private String BASE_URL;

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

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
        // crear usuarios por test
        User admin = new User("Admin", "admin@example.com", passwordEncoder.encode("adminpass"), Role.ADMINISTRADOR);
        User user = new User("User", "user@example.com", passwordEncoder.encode("userpass"), Role.JUGADOR);
        userRepository.save(admin);
        userRepository.save(user);
    }

    @Test
    void testUserDataAdmin() {
        JwtAuthenticationResponse admin = login("admin@example.com", "adminpass");
        String token = admin.getToken();

        RequestEntity<Void> req = RequestEntity.get(BASE_URL + "/api/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        ResponseEntity<Map> resp = rest.exchange(req, Map.class);
        assertTrue(resp.getStatusCode().is2xxSuccessful());
        Map body = resp.getBody();
        assertNotNull(body);
        assertEquals("admin@example.com", body.get("email"));
        assertEquals("ADMINISTRADOR", body.get("role"));
    }

    @Test
    void testUserDataPlayer() {
        JwtAuthenticationResponse user = login("user@example.com", "userpass");
        String token = user.getToken();

        RequestEntity<Void> req = RequestEntity.get(BASE_URL + "/api/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        ResponseEntity<Map> resp = rest.exchange(req, Map.class);
        assertTrue(resp.getStatusCode().is2xxSuccessful());
        Map body = resp.getBody();
        assertNotNull(body);
        assertEquals("user@example.com", body.get("email"));
        assertEquals("JUGADOR", body.get("role"));
    }

    @Test
    void testUserDataWithoutLogin() {
        RequestEntity<Void> req = RequestEntity.get(BASE_URL + "/api/user/me").build();
        ResponseEntity<String> resp = rest.exchange(req, String.class);
        assertTrue(resp.getStatusCode().is4xxClientError());
    }
}