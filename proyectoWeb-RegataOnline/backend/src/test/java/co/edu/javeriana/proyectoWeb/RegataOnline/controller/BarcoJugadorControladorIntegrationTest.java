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

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.BarcoJugadorDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Barco;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Jugador;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.BarcoRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.JugadorRepositorio;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration-testing")
public class BarcoJugadorControladorIntegrationTest {

	private String SERVER_URL;

	@Autowired
	private JugadorRepositorio jugadorRepositorio;

	@Autowired
	private BarcoRepositorio barcoRepositorio;

	@Autowired
	private WebTestClient webTestClient;

	public BarcoJugadorControladorIntegrationTest(@Value("${server.port}") int serverPort) {
		this.SERVER_URL = "http://localhost:" + serverPort + "/";
	}

	@BeforeEach
	void init() {
		Jugador j = new Jugador("Jugador1");
		jugadorRepositorio.save(j);

		Barco b = new Barco("Barco1", 0, 0, 0, 0);
		b.setJugador(j);
		barcoRepositorio.save(b);
	}

	@Test
	void testGetBarcosDeJugadorExistente() {
		Jugador j = jugadorRepositorio.findAll().get(0);
		Long jugadorId = j.getId();

		webTestClient.get().uri(SERVER_URL + "jugador/barcos/" + jugadorId)
			.exchange()
			.expectStatus().isOk()
			.expectBody(BarcoJugadorDTO.class)
			.value(dto -> {
				assertEquals(jugadorId, dto.getJugadorId());
				assertEquals(1, dto.getBarcosIds().size());
			});
	}

	@Test
	void testGetBarcosDeJugadorNoExistente() {
		webTestClient.get().uri(SERVER_URL + "jugador/barcos/99999")
			.exchange()
			.expectStatus().isOk()
			.expectBody().consumeWith(res -> {
				byte[] body = res.getResponseBody();
				if (body != null && body.length > 0) {
					String s = new String(body);
					assertEquals("null", s.trim());
				}
			});
	}

	@Test
	void testActualizarBarcosDeJugadorVacio() {
		Jugador j = jugadorRepositorio.findAll().get(0);
		Long jugadorId = j.getId();

		BarcoJugadorDTO dto = new BarcoJugadorDTO();
		dto.setJugadorId(jugadorId);
		dto.setBarcosIds(List.of());

		webTestClient.post().uri(SERVER_URL + "jugador/barcos/save")
			.bodyValue(dto)
			.exchange()
			.expectStatus().isOk();

		Jugador updated = jugadorRepositorio.findById(jugadorId).orElseThrow();
		long asociados = barcoRepositorio.findAll().stream()
			.filter(b -> b.getJugador() != null && b.getJugador().getId().equals(jugadorId))
			.count();

		assertEquals(0, asociados);
	}

	@Test
	void testActualizarBarcosDeJugadorAsignarBarco() {
		Jugador j = jugadorRepositorio.findAll().get(0);
		Long jugadorId = j.getId();

		// crear un barco sin jugador
		Barco otro = new Barco("Barco2", 0, 0, 0, 0);
		barcoRepositorio.save(otro);

		BarcoJugadorDTO dto = new BarcoJugadorDTO();
		dto.setJugadorId(jugadorId);
		dto.setBarcosIds(List.of(otro.getId()));

		webTestClient.post().uri(SERVER_URL + "jugador/barcos/save")
			.bodyValue(dto)
			.exchange()
			.expectStatus().isOk();

		Barco actualizado = barcoRepositorio.findById(otro.getId()).orElseThrow();
		assertEquals(jugadorId, actualizado.getJugador().getId());
	}
}
