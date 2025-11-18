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

import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoJugadorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.JwtAuthenticationResponse;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.LoginDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Barco;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Jugador;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Role;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.User;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.BarcoRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.JugadorRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.UserRepository;


@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration-testing")
public class BarcoJugadorControladorIntegrationTest {

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

		Jugador j = new Jugador("Jugador1");
		jugadorRepositorio.save(j);

		Barco b = new Barco("Barco1", 0, 0, 0, 0);
		b.setJugador(j);
		barcoRepositorio.save(b);
	}

	@Test
	void testGetBarcosDeJugadorExistente() throws Exception {
		JwtAuthenticationResponse adminLogin = login("admin@example.com", "adminpass");
		String token = adminLogin.getToken();
		Jugador j = jugadorRepositorio.findAll().get(0);
		Long jugadorId = j.getId();

		RequestEntity<Void> req = RequestEntity.get(BASE_URL + "/jugador/barcos/" + jugadorId)
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
			.build();
		ResponseEntity<String> resp = rest.exchange(req, String.class);
		assertTrue(resp.getStatusCode().is2xxSuccessful(), "La respuesta debe ser exitosa");
		String rawBody = resp.getBody();
		assertNotNull(rawBody, "El cuerpo de la respuesta no puede ser null");
		ObjectMapper mapper = new ObjectMapper();
		BarcoJugadorDTO dto = mapper.readValue(rawBody, BarcoJugadorDTO.class);
		assertNotNull(dto, "El DTO de respuesta no puede ser null");
		assertEquals(jugadorId, dto.getJugadorId());
		assertEquals(1, dto.getBarcosIds().size());
	}

	@Test
	void testGetBarcosDeJugadorNoExistente() {
		JwtAuthenticationResponse adminLogin = login("admin@example.com", "adminpass");
		String token = adminLogin.getToken();
		RequestEntity<Void> req = RequestEntity.get(BASE_URL + "/jugador/barcos/99999")
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
			.build();
		ResponseEntity<String> resp = rest.exchange(req, String.class);
		assertTrue(resp.getStatusCode().is2xxSuccessful(), "La respuesta debe ser exitosa");
		String body = resp.getBody();
		// Puede ser null si no existe
		if (body != null) assertEquals("null", body.trim());
	}

	@Test
	void testActualizarBarcosDeJugadorVacio() {
		JwtAuthenticationResponse adminLogin = login("admin@example.com", "adminpass");
		String token = adminLogin.getToken();
		Jugador j = jugadorRepositorio.findAll().get(0);
		Long jugadorId = j.getId();

		BarcoJugadorDTO dto = new BarcoJugadorDTO();
		dto.setJugadorId(jugadorId);
		dto.setBarcosIds(List.of());

		RequestEntity<BarcoJugadorDTO> req = RequestEntity.post(BASE_URL + "/jugador/barcos/save")
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
			.body(dto);
		ResponseEntity<Void> resp = rest.exchange(req, Void.class);
		assertTrue(resp.getStatusCode().is2xxSuccessful());

		long asociados = barcoRepositorio.findAll().stream()
			.filter(b -> b.getJugador() != null && b.getJugador().getId().equals(jugadorId))
			.count();
		assertEquals(0, asociados);
	}

	@Test
	void testActualizarBarcosDeJugadorAsignarBarco() {
		JwtAuthenticationResponse adminLogin = login("admin@example.com", "adminpass");
		String token = adminLogin.getToken();
		Jugador j = jugadorRepositorio.findAll().get(0);
		Long jugadorId = j.getId();

		// crear un barco sin jugador
		Barco otro = new Barco("Barco2", 0, 0, 0, 0);
		barcoRepositorio.save(otro);

		BarcoJugadorDTO dto = new BarcoJugadorDTO();
		dto.setJugadorId(jugadorId);
		dto.setBarcosIds(List.of(otro.getId()));

		RequestEntity<BarcoJugadorDTO> req = RequestEntity.post(BASE_URL + "/jugador/barcos/save")
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
			.body(dto);
		ResponseEntity<Void> resp = rest.exchange(req, Void.class);
		assertTrue(resp.getStatusCode().is2xxSuccessful());

		Barco actualizado = barcoRepositorio.findById(otro.getId()).orElseThrow();
		assertEquals(jugadorId, actualizado.getJugador().getId());
	}

	@Test
	void barcosDeJugadorSinLogin() {
		Jugador j = jugadorRepositorio.findAll().get(0);
		Long jugadorId = j.getId();

		RequestEntity<Void> req = RequestEntity.get(BASE_URL + "/jugador/barcos/" + jugadorId)
			.build();
		ResponseEntity<String> resp = rest.exchange(req, String.class);
		assertEquals(HttpStatus.FORBIDDEN, resp.getStatusCode());
	}

	@Test
	void barcosDeJugadorUsuarioNoAutorizado() {
		JwtAuthenticationResponse userLogin = login("user@example.com", "userpass");
		String token = userLogin.getToken();
		Jugador j = jugadorRepositorio.findAll().get(0);
		Long jugadorId = j.getId();

		RequestEntity<Void> req = RequestEntity.get(BASE_URL + "/jugador/barcos/" + jugadorId)
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
			.build();
		ResponseEntity<String> resp = rest.exchange(req, String.class);
		assertEquals(HttpStatus.FORBIDDEN, resp.getStatusCode());
	}
}
		