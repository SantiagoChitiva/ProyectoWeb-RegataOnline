package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.CrearPartidaRequest;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.JwtAuthenticationResponse;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.LoginDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.MovimientoDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.PartidaDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.RealizarMovimientoRequest;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.UnirsePartidaRequest;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Barco;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Celda;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Jugador;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Mapa;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Role;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.User;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.BarcoRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.CeldaRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.JugadorRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.MapaRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.UserRepository;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.BrowserType;

import org.junit.jupiter.api.AfterEach;


@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("system-testing")
public class PartidaMultijugadorControladorSystemTest {

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
    private MapaRepositorio mapaRepositorio;

    @Autowired
    private CeldaRepositorio celdaRepositorio;

    @Autowired
    private BarcoRepositorio barcoRepositorio;
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;
private String SERVER_URL;

    private JwtAuthenticationResponse login(String email, String password) {
        RequestEntity<LoginDTO> request = RequestEntity.post(BASE_URL + "/auth/login")
                .body(new LoginDTO(email, password));
        ResponseEntity<JwtAuthenticationResponse> jwtResponse = rest.exchange(request, JwtAuthenticationResponse.class);
        JwtAuthenticationResponse body = jwtResponse.getBody();
        assertNotNull(body, "La respuesta del login no puede ser null");
        return body;
    }

    private Long jugador1Id, jugador2Id, jugador3Id, jugador4Id;
    private Long mapaId;
    private Long barco1Id, barco2Id, barco3Id, barco4Id;

    @BeforeEach
    void init() {
        // Crear usuarios de prueba
        User admin = new User("Admin", "admin@example.com", passwordEncoder.encode("adminpass"), Role.ADMINISTRADOR);
        User user1 = new User("User1", "user1@example.com", passwordEncoder.encode("user1pass"), Role.JUGADOR);
        User user2 = new User("User2", "user2@example.com", passwordEncoder.encode("user2pass"), Role.JUGADOR);
        User user3 = new User("User3", "user3@example.com", passwordEncoder.encode("user3pass"), Role.JUGADOR);
        User user4 = new User("User4", "user4@example.com", passwordEncoder.encode("user4pass"), Role.JUGADOR);
        userRepository.save(admin);
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);

        // Crear 4 jugadores
        Jugador j1 = jugadorRepositorio.save(new Jugador("Jugador1"));
        Jugador j2 = jugadorRepositorio.save(new Jugador("Jugador2"));
        Jugador j3 = jugadorRepositorio.save(new Jugador("Jugador3"));
        Jugador j4 = jugadorRepositorio.save(new Jugador("Jugador4"));
        jugador1Id = j1.getId();
        jugador2Id = j2.getId();
        jugador3Id = j3.getId();
        jugador4Id = j4.getId();

        // Crear mapa 5x5 con puerto (P), mar (M) y tierra (x)
        Mapa m = mapaRepositorio.save(new Mapa(5, 5));
        mapaId = m.getId();
        
        // Fila 0: P M M M M (puerto en 0,0)
        Celda c00 = new Celda("P", 0, 0);
        c00.setMapa(m);
        celdaRepositorio.save(c00);
        
        Celda c01 = new Celda("M", 0, 1);
        c01.setMapa(m);
        celdaRepositorio.save(c01);
        
        Celda c02 = new Celda("M", 0, 2);
        c02.setMapa(m);
        celdaRepositorio.save(c02);
        
        Celda c03 = new Celda("M", 0, 3);
        c03.setMapa(m);
        celdaRepositorio.save(c03);
        
        Celda c04 = new Celda("M", 0, 4);
        c04.setMapa(m);
        celdaRepositorio.save(c04);
        
        // Fila 1: M M M M M
        for (int col = 0; col < 5; col++) {
            Celda c = new Celda("M", 1, col);
            c.setMapa(m);
            celdaRepositorio.save(c);
        }
        
        // Fila 2: M M x M M (tierra en 2,2)
        Celda c20 = new Celda("M", 2, 0);
        c20.setMapa(m);
        celdaRepositorio.save(c20);
        
        Celda c21 = new Celda("M", 2, 1);
        c21.setMapa(m);
        celdaRepositorio.save(c21);
        
        Celda c22 = new Celda("x", 2, 2);
        c22.setMapa(m);
        celdaRepositorio.save(c22);
        
        Celda c23 = new Celda("M", 2, 3);
        c23.setMapa(m);
        celdaRepositorio.save(c23);
        
        Celda c24 = new Celda("M", 2, 4);
        c24.setMapa(m);
        celdaRepositorio.save(c24);
        
        // Fila 3: M M M M M
        for (int col = 0; col < 5; col++) {
            Celda c = new Celda("M", 3, col);
            c.setMapa(m);
            celdaRepositorio.save(c);
        }
        
        // Fila 4: M M M M P (meta en 4,4)
        Celda c40 = new Celda("M", 4, 0);
        c40.setMapa(m);
        celdaRepositorio.save(c40);
        
        Celda c41 = new Celda("M", 4, 1);
        c41.setMapa(m);
        celdaRepositorio.save(c41);
        
        Celda c42 = new Celda("M", 4, 2);
        c42.setMapa(m);
        celdaRepositorio.save(c42);
        
        Celda c43 = new Celda("M", 4, 3);
        c43.setMapa(m);
        celdaRepositorio.save(c43);
        
        Celda c44 = new Celda("P", 4, 4);
        c44.setMapa(m);
        celdaRepositorio.save(c44);

        // Crear 4 barcos diferentes (posición inicial en puerto 0,0)
        Barco b1 = barcoRepositorio.save(new Barco("Barco1", 0, 0, 0, 0));
        Barco b2 = barcoRepositorio.save(new Barco("Barco2", 0, 0, 0, 0));
        Barco b3 = barcoRepositorio.save(new Barco("Barco3", 0, 0, 0, 0));
        Barco b4 = barcoRepositorio.save(new Barco("Barco4", 0, 0, 0, 0));
        barco1Id = b1.getId();
        barco2Id = b2.getId();
        barco3Id = b3.getId();
        barco4Id = b4.getId();
        this.playwright = Playwright.create();
        this.browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        this.context = browser.newContext();
        this.page = context.newPage();  
        this.SERVER_URL = "http://localhost:4200";
    }
    @AfterEach
    void end(){
        browser.close();
        playwright.close();
    }

@Test
void prueba1(){
        page.navigate(SERVER_URL);
}
    @Test
    void testCrearPartidaMultijugadorConAdmin() throws Exception {
        JwtAuthenticationResponse adminLogin = login("admin@example.com", "adminpass");
        String token = adminLogin.getToken();

        CrearPartidaRequest req = new CrearPartidaRequest();
        req.setJugadorId(jugador1Id);
        req.setMapaId(mapaId);
        req.setBarcoId(barco1Id);

        RequestEntity<CrearPartidaRequest> createReq = RequestEntity.post(BASE_URL + "/partida-multijugador/crear")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(req);
        ResponseEntity<PartidaDTO> resp = rest.exchange(createReq, PartidaDTO.class);
        
        assertTrue(resp.getStatusCode().is2xxSuccessful());
        PartidaDTO partida = resp.getBody();
        assertNotNull(partida);
        assertNotNull(partida.getId());
        assertEquals("esperando", partida.getEstado());
        assertEquals(1, partida.getCantidadJugadores());
    }

    @Test
    void testCrearPartidaMultijugadorConJugador() throws Exception {
        JwtAuthenticationResponse userLogin = login("user1@example.com", "user1pass");
        String token = userLogin.getToken();

        CrearPartidaRequest req = new CrearPartidaRequest();
        req.setJugadorId(jugador1Id);
        req.setMapaId(mapaId);
        req.setBarcoId(barco1Id);

        RequestEntity<CrearPartidaRequest> createReq = RequestEntity.post(BASE_URL + "/partida-multijugador/crear")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(req);
        ResponseEntity<PartidaDTO> resp = rest.exchange(createReq, PartidaDTO.class);
        
        assertTrue(resp.getStatusCode().is2xxSuccessful());
        PartidaDTO partida = resp.getBody();
        assertNotNull(partida);
        assertEquals("esperando", partida.getEstado());
    }

    @Test
    void testUnirseAPartida() throws Exception {
        // Jugador 1 crea la partida
        JwtAuthenticationResponse user1Login = login("user1@example.com", "user1pass");
        String token1 = user1Login.getToken();

        CrearPartidaRequest crearReq = new CrearPartidaRequest();
        crearReq.setJugadorId(jugador1Id);
        crearReq.setMapaId(mapaId);
        crearReq.setBarcoId(barco1Id);

        RequestEntity<CrearPartidaRequest> createReq = RequestEntity.post(BASE_URL + "/partida-multijugador/crear")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                .body(crearReq);
        ResponseEntity<PartidaDTO> crearResp = rest.exchange(createReq, PartidaDTO.class);
        PartidaDTO partida = crearResp.getBody();
        assertNotNull(partida);
        Long partidaId = partida.getId();

        // Jugador 2 se une
        JwtAuthenticationResponse user2Login = login("user2@example.com", "user2pass");
        String token2 = user2Login.getToken();

        UnirsePartidaRequest unirseReq = new UnirsePartidaRequest();
        unirseReq.setJugadorId(jugador2Id);
        unirseReq.setBarcoId(barco2Id);

        RequestEntity<UnirsePartidaRequest> joinReq = RequestEntity.post(BASE_URL + "/partida-multijugador/" + partidaId + "/unirse")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token2)
                .body(unirseReq);
        ResponseEntity<PartidaDTO> joinResp = rest.exchange(joinReq, PartidaDTO.class);
        
        assertTrue(joinResp.getStatusCode().is2xxSuccessful());
        PartidaDTO partidaActualizada = joinResp.getBody();
        assertNotNull(partidaActualizada);
        assertEquals(2, partidaActualizada.getCantidadJugadores());
    }

    @Test
    void testIniciarPartida() throws Exception {
        // Jugador 1 crea la partida
        JwtAuthenticationResponse user1Login = login("user1@example.com", "user1pass");
        String token1 = user1Login.getToken();

        CrearPartidaRequest crearReq = new CrearPartidaRequest();
        crearReq.setJugadorId(jugador1Id);
        crearReq.setMapaId(mapaId);
        crearReq.setBarcoId(barco1Id);

        RequestEntity<CrearPartidaRequest> createReq = RequestEntity.post(BASE_URL + "/partida-multijugador/crear")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                .body(crearReq);
        ResponseEntity<PartidaDTO> crearResp = rest.exchange(createReq, PartidaDTO.class);
        Long partidaId = crearResp.getBody().getId();

        // Jugador 2 se une
        JwtAuthenticationResponse user2Login = login("user2@example.com", "user2pass");
        String token2 = user2Login.getToken();

        UnirsePartidaRequest unirseReq = new UnirsePartidaRequest();
        unirseReq.setJugadorId(jugador2Id);
        unirseReq.setBarcoId(barco2Id);

        RequestEntity<UnirsePartidaRequest> joinReq = RequestEntity.post(BASE_URL + "/partida-multijugador/" + partidaId + "/unirse")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token2)
                .body(unirseReq);
        rest.exchange(joinReq, PartidaDTO.class);

        // Jugador 1 inicia la partida
        RequestEntity<Void> iniciarReq = RequestEntity.post(BASE_URL + "/partida-multijugador/" + partidaId + "/iniciar")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                .build();
        ResponseEntity<PartidaDTO> iniciarResp = rest.exchange(iniciarReq, PartidaDTO.class);
        
        assertTrue(iniciarResp.getStatusCode().is2xxSuccessful());
        PartidaDTO partidaIniciada = iniciarResp.getBody();
        assertNotNull(partidaIniciada);
        assertEquals("en_curso", partidaIniciada.getEstado());
    }

    @Test
    void testRealizarMovimiento() throws Exception {
        // Crear y iniciar partida con 2 jugadores
        JwtAuthenticationResponse user1Login = login("user1@example.com", "user1pass");
        String token1 = user1Login.getToken();

        CrearPartidaRequest crearReq = new CrearPartidaRequest();
        crearReq.setJugadorId(jugador1Id);
        crearReq.setMapaId(mapaId);
        crearReq.setBarcoId(barco1Id);

        RequestEntity<CrearPartidaRequest> createReq = RequestEntity.post(BASE_URL + "/partida-multijugador/crear")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                .body(crearReq);
        ResponseEntity<PartidaDTO> crearResp = rest.exchange(createReq, PartidaDTO.class);
        Long partidaId = crearResp.getBody().getId();

        // Jugador 2 se une
        JwtAuthenticationResponse user2Login = login("user2@example.com", "user2pass");
        String token2 = user2Login.getToken();

        UnirsePartidaRequest unirseReq = new UnirsePartidaRequest();
        unirseReq.setJugadorId(jugador2Id);
        unirseReq.setBarcoId(barco2Id);

        RequestEntity<UnirsePartidaRequest> joinReq = RequestEntity.post(BASE_URL + "/partida-multijugador/" + partidaId + "/unirse")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token2)
                .body(unirseReq);
        rest.exchange(joinReq, PartidaDTO.class);

        // Iniciar partida
        RequestEntity<Void> iniciarReq = RequestEntity.post(BASE_URL + "/partida-multijugador/" + partidaId + "/iniciar")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                .build();
        rest.exchange(iniciarReq, PartidaDTO.class);

        // Jugador 1 realiza primer movimiento (aceleración 1,1)
        RealizarMovimientoRequest movReq = new RealizarMovimientoRequest();
        movReq.setJugadorId(jugador1Id);
        movReq.setAceleracionX(1);
        movReq.setAceleracionY(1);

        RequestEntity<RealizarMovimientoRequest> moverReq = RequestEntity.post(BASE_URL + "/partida-multijugador/" + partidaId + "/mover")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                .body(movReq);
        ResponseEntity<MovimientoDTO> moverResp = rest.exchange(moverReq, MovimientoDTO.class);
        
        assertTrue(moverResp.getStatusCode().is2xxSuccessful());
        MovimientoDTO movimiento = moverResp.getBody();
        assertNotNull(movimiento);
        assertNotNull(movimiento.getId());
    }

    @Test
    void testObtenerPartida() throws Exception {
        // Crear partida
        JwtAuthenticationResponse user1Login = login("user1@example.com", "user1pass");
        String token = user1Login.getToken();

        CrearPartidaRequest crearReq = new CrearPartidaRequest();
        crearReq.setJugadorId(jugador1Id);
        crearReq.setMapaId(mapaId);
        crearReq.setBarcoId(barco1Id);

        RequestEntity<CrearPartidaRequest> createReq = RequestEntity.post(BASE_URL + "/partida-multijugador/crear")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(crearReq);
        ResponseEntity<PartidaDTO> crearResp = rest.exchange(createReq, PartidaDTO.class);
        Long partidaId = crearResp.getBody().getId();

        // Obtener partida
        RequestEntity<Void> getReq = RequestEntity.get(BASE_URL + "/partida-multijugador/" + partidaId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        ResponseEntity<PartidaDTO> getResp = rest.exchange(getReq, PartidaDTO.class);
        
        assertTrue(getResp.getStatusCode().is2xxSuccessful());
        PartidaDTO partida = getResp.getBody();
        assertNotNull(partida);
        assertEquals(partidaId, partida.getId());
    }

    @Test
    void testListarPartidasDisponibles() throws Exception {
        // Crear partida disponible
        JwtAuthenticationResponse user1Login = login("user1@example.com", "user1pass");
        String token1 = user1Login.getToken();

        CrearPartidaRequest crearReq = new CrearPartidaRequest();
        crearReq.setJugadorId(jugador1Id);
        crearReq.setMapaId(mapaId);
        crearReq.setBarcoId(barco1Id);

        RequestEntity<CrearPartidaRequest> createReq = RequestEntity.post(BASE_URL + "/partida-multijugador/crear")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                .body(crearReq);
        rest.exchange(createReq, PartidaDTO.class);

        // Listar partidas disponibles
        JwtAuthenticationResponse user2Login = login("user2@example.com", "user2pass");
        String token2 = user2Login.getToken();

        RequestEntity<Void> listReq = RequestEntity.get(BASE_URL + "/partida-multijugador/disponibles")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token2)
                .build();
        ResponseEntity<List<PartidaDTO>> listResp = rest.exchange(listReq, new ParameterizedTypeReference<List<PartidaDTO>>() {});
        
        assertTrue(listResp.getStatusCode().is2xxSuccessful());
        List<PartidaDTO> partidas = listResp.getBody();
        assertNotNull(partidas);
        assertTrue(partidas.size() > 0);
        assertEquals("esperando", partidas.get(0).getEstado());
    }

    @Test
    void testObtenerMovimientosJugador() throws Exception {
        // Crear, iniciar y realizar movimiento
        JwtAuthenticationResponse user1Login = login("user1@example.com", "user1pass");
        String token1 = user1Login.getToken();

        CrearPartidaRequest crearReq = new CrearPartidaRequest();
        crearReq.setJugadorId(jugador1Id);
        crearReq.setMapaId(mapaId);
        crearReq.setBarcoId(barco1Id);

        RequestEntity<CrearPartidaRequest> createReq = RequestEntity.post(BASE_URL + "/partida-multijugador/crear")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                .body(crearReq);
        ResponseEntity<PartidaDTO> crearResp = rest.exchange(createReq, PartidaDTO.class);
        Long partidaId = crearResp.getBody().getId();

        // Jugador 2 se une
        JwtAuthenticationResponse user2Login = login("user2@example.com", "user2pass");
        String token2 = user2Login.getToken();

        UnirsePartidaRequest unirseReq = new UnirsePartidaRequest();
        unirseReq.setJugadorId(jugador2Id);
        unirseReq.setBarcoId(barco2Id);

        RequestEntity<UnirsePartidaRequest> joinReq = RequestEntity.post(BASE_URL + "/partida-multijugador/" + partidaId + "/unirse")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token2)
                .body(unirseReq);
        rest.exchange(joinReq, PartidaDTO.class);

        // Iniciar
        RequestEntity<Void> iniciarReq = RequestEntity.post(BASE_URL + "/partida-multijugador/" + partidaId + "/iniciar")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                .build();
        rest.exchange(iniciarReq, PartidaDTO.class);

        // Mover
        RealizarMovimientoRequest movReq = new RealizarMovimientoRequest();
        movReq.setJugadorId(jugador1Id);
        movReq.setAceleracionX(1);
        movReq.setAceleracionY(0);

        RequestEntity<RealizarMovimientoRequest> moverReq = RequestEntity.post(BASE_URL + "/partida-multijugador/" + partidaId + "/mover")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                .body(movReq);
        rest.exchange(moverReq, MovimientoDTO.class);

        // Obtener movimientos
        RequestEntity<Void> getMovsReq = RequestEntity.get(BASE_URL + "/partida-multijugador/" + partidaId + "/movimientos/" + jugador1Id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                .build();
        ResponseEntity<List<MovimientoDTO>> getMovsResp = rest.exchange(getMovsReq, new ParameterizedTypeReference<List<MovimientoDTO>>() {});
        
        assertTrue(getMovsResp.getStatusCode().is2xxSuccessful());
        List<MovimientoDTO> movimientos = getMovsResp.getBody();
        assertNotNull(movimientos);
        assertTrue(movimientos.size() > 0);
    }

    @Test
    void testCrearPartidaSinAutenticacion() {
        CrearPartidaRequest req = new CrearPartidaRequest();
        req.setJugadorId(jugador1Id);
        req.setMapaId(mapaId);
        req.setBarcoId(barco1Id);

        RequestEntity<CrearPartidaRequest> createReq = RequestEntity.post(BASE_URL + "/partida-multijugador/crear")
                .body(req);
        ResponseEntity<String> resp = rest.exchange(createReq, String.class);
        
        assertTrue(resp.getStatusCode().is4xxClientError());
    }

    @Test
    void testUnirseAPartidaLlena() throws Exception {
        // Crear partida con jugador 1
        JwtAuthenticationResponse user1Login = login("user1@example.com", "user1pass");
        String token1 = user1Login.getToken();

        CrearPartidaRequest crearReq = new CrearPartidaRequest();
        crearReq.setJugadorId(jugador1Id);
        crearReq.setMapaId(mapaId);
        crearReq.setBarcoId(barco1Id);

        RequestEntity<CrearPartidaRequest> createReq = RequestEntity.post(BASE_URL + "/partida-multijugador/crear")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token1)
                .body(crearReq);
        ResponseEntity<PartidaDTO> crearResp = rest.exchange(createReq, PartidaDTO.class);
        Long partidaId = crearResp.getBody().getId();

        // Unir jugador 2, 3, 4 (máximo 4 jugadores)
        String[] emails = {"user2@example.com", "user3@example.com", "user4@example.com"};
        Long[] jugadorIds = {jugador2Id, jugador3Id, jugador4Id};
        Long[] barcoIds = {barco2Id, barco3Id, barco4Id};

        for (int i = 0; i < 3; i++) {
            JwtAuthenticationResponse userLogin = login(emails[i], emails[i].replace("@example.com", "pass"));
            String token = userLogin.getToken();

            UnirsePartidaRequest unirseReq = new UnirsePartidaRequest();
            unirseReq.setJugadorId(jugadorIds[i]);
            unirseReq.setBarcoId(barcoIds[i]);

            RequestEntity<UnirsePartidaRequest> joinReq = RequestEntity.post(BASE_URL + "/partida-multijugador/" + partidaId + "/unirse")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .body(unirseReq);
            rest.exchange(joinReq, PartidaDTO.class);
        }

        // Intentar unir un 5to jugador (debe fallar)
        Jugador j5 = jugadorRepositorio.save(new Jugador("Jugador5"));
        Barco b5 = barcoRepositorio.save(new Barco("Barco5", 0, 0, 0, 0));
        User user5 = new User("User5", "user5@example.com", passwordEncoder.encode("user5pass"), Role.JUGADOR);
        userRepository.save(user5);

        JwtAuthenticationResponse user5Login = login("user5@example.com", "user5pass");
        String token5 = user5Login.getToken();

        UnirsePartidaRequest unirseReq5 = new UnirsePartidaRequest();
        unirseReq5.setJugadorId(j5.getId());
        unirseReq5.setBarcoId(b5.getId());

        RequestEntity<UnirsePartidaRequest> joinReq5 = RequestEntity.post(BASE_URL + "/partida-multijugador/" + partidaId + "/unirse")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token5)
                .body(unirseReq5);
        ResponseEntity<String> joinResp5 = rest.exchange(joinReq5, String.class);
        
        assertTrue(joinResp5.getStatusCode().is4xxClientError());
    }
}
