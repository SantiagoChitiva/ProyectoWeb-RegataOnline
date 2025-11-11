package co.edu.javeriana.proyectoWeb.RegataOnline.init;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import co.edu.javeriana.proyectoWeb.RegataOnline.model.Barco;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Celda;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Jugador;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Mapa;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Modelo;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Role;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.User;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.BarcoRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.CeldaRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.JugadorRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.MapaRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.ModeloRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.UserRepository;


@Profile({"default"})
@Component
public class DbInitializer implements CommandLineRunner {

    @Autowired
    private ModeloRepositorio modeloRepositorio;

    @Autowired
    private BarcoRepositorio barcoRepositorio;

    @Autowired
    private JugadorRepositorio jugadorRepositorio;

    @Autowired
    private MapaRepositorio mapaRepositorio;
    
    @Autowired
    private CeldaRepositorio celdaRepositorio;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Logger log = LoggerFactory.getLogger(getClass());

    private final String[] nombresBarcos = {
        "Viento del Mar", "Águila Marina", "Tormenta Azul", "Navegante Dorado", "Estrella del Norte",
        "Corsario Veloz", "Tsunami", "Brisa Oceánica", "Rayo del Caribe", "Sirena Plateada",
        "Huracán Negro", "Delfín Saltarín", "Marejada", "Capitán Audaz", "Espuma Blanca",
        "Trueno Marino", "Ola Gigante", "Velero Fantasma", "Kraken", "Perla Negra",
        "Tiburón Blanco", "Marea Alta", "Viento Norte", "Capitán Garfio", "Mar Bravío",
        "Aventurero", "Explorador", "Conquistador", "Navegador", "Pirata Real",
        "Océano Profundo", "Corriente Marina", "Barlovento", "Sotavento", "Naufragio",
        "Tesoro Perdido", "Isla Misteriosa", "Puerto Seguro", "Faro Luminoso", "Ancla de Oro",
        "Velamen Real", "Timón de Plata", "Brújula Mágica", "Cataviento", "Bauprés",
        "Mastelero", "Jarcia Firme", "Driza Maestra", "Escota Libre", "Amura de Estribor"
    };

    @Override
    public void run(String... args) throws Exception {
        // ========================================
        // CREAR USUARIOS DE AUTENTICACIÓN
        // ========================================
        log.info("🔐 Inicializando usuarios de autenticación...");
        crearUsuarioAdministrador();
        crearUsuarioJugadorPrueba();
        
        // ========================================
        // CREAR JUGADORES Y MODELOS
        // ========================================
        List<Jugador> jugadores = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            Jugador jugador = jugadorRepositorio.save(new Jugador("Jugador " + i, "jugador" + i + "@test.com"));
            jugadores.add(jugador);
        }
        for(int i = 0; i < 10; i++) {
            modeloRepositorio.save(new Modelo("Modelo " + i, "Color " + i));
        }  
        List<Barco> barcos = new ArrayList<>();
        int barcoIndex = 0;
        for (int j = 0; j < jugadores.size(); j++) {
            Jugador jugador = jugadores.get(j);
            List<Barco> barcosJugador = new ArrayList<>();

            for (int b = 0; b < 10; b++) {
                String nombreBarco = nombresBarcos[barcoIndex % nombresBarcos.length];
                // Todos los barcos inician en posición (0,0) con velocidad (0,0)
                Barco barco = new Barco(nombreBarco, 0, 0, 0, 0);
                barco.setJugador(jugador);
                barco.setModelo(modeloRepositorio.findById(barcoIndex % 10L).orElse(null));
                barco = barcoRepositorio.save(barco);
                barcosJugador.add(barco);
                barcoIndex++;
            }

            jugador.setBarcos(barcosJugador);
            jugadorRepositorio.save(jugador);
        }

        // Mapa de prueba 5x5 (FÁCIL - para probar física)
        log.info("Creando mapa de prueba 5x5...");
        Mapa mapaPrueba = new Mapa(5, 5);
        mapaPrueba = mapaRepositorio.save(mapaPrueba);

        List<Celda> celdasPrueba = new ArrayList<>();
        
        // Crear mapa 5x5: todo el contorno son paredes, Partida en (1,1), Meta en (3,3)
        for (int fila = 0; fila < 5; fila++) {
            for (int col = 0; col < 5; col++) {
                String tipo = "";
                
                // Todo el contorno son paredes
                if (fila == 0 || fila == 4 || col == 0 || col == 4) {
                    tipo = "x";
                }
                // Celda de partida en (1, 1)
                else if (fila == 1 && col == 1) {
                    tipo = "P";
                }
                // Celda de meta en (3, 3)
                else if (fila == 3 && col == 3) {
                    tipo = "M";
                }
                // Resto son agua
                
                Celda celda = new Celda(tipo, col, fila);
                celda.setMapa(mapaPrueba);
                celda = celdaRepositorio.save(celda);
                celdasPrueba.add(celda);
            }
        }
        
        log.info("Mapa de prueba 5x5 creado - Partida:(1,1) Meta:(3,3)");

        // Mapa principal 10x10 (DIFÍCIL - para juego completo)
        log.info("Creando mapa principal 10x10...");
        Mapa mapa = new Mapa(10, 10);
        mapa = mapaRepositorio.save(mapa);

        List<Celda> celdas = new ArrayList<>();
        
        // Crear todas las celdas del mapa (10 filas x 10 columnas = 100 celdas)
        for (int fila = 0; fila < 10; fila++) {
            for (int col = 0; col < 10; col++) {
                String tipo = "";
                
                // Todo el contorno son paredes, EXCEPTO fila 9
                if (fila == 0 || col == 0 || col == 9) {
                    tipo = "x";
                }
                // Fila 9: solo las esquinas son paredes
                else if (fila == 9 && (col == 0 || col == 9)) {
                    tipo = "x";
                }
                // Celda de partida en (1, 1)
                else if (fila == 1 && col == 1) {
                    tipo = "P";
                }
                // Celda de meta en (8, 8)
                else if (fila == 8 && col == 8) {
                    tipo = "M";
                }
                
                Celda celda = new Celda(tipo, col, fila);
                celda.setMapa(mapa);
                celda = celdaRepositorio.save(celda);
                celdas.add(celda);
            }
        }
        
        log.info("Mapa principal 10x10 creado - Partida:(1,1) Meta:(8,8)");

        List<Barco> todosLosBarcos = barcoRepositorio.findAll();
        List<Celda> celdasNavegables = celdas.stream()
            .filter(c -> c.esAgua() || c.esPartida())
            .collect(Collectors.toList());

        for (int i = 0; i < Math.min(todosLosBarcos.size(), celdasNavegables.size()); i++) {
            Barco barco = todosLosBarcos.get(i);
            Celda celda = celdasNavegables.get(i % celdasNavegables.size());
            barco.setCelda(celda);
            barcoRepositorio.save(barco);
        }
    }

    private void crearUsuarioAdministrador() {
        String adminEmail = "admin@regata.com";
        
        if (userRepository.existsByEmail(adminEmail)) {
            log.info("👤 Usuario administrador ya existe: {}", adminEmail);
            return;
        }
        
        User admin = new User(
            "Administrador",
            adminEmail,
            passwordEncoder.encode("admin123"),
            Role.ADMINISTRADOR
        );
        
        userRepository.save(admin);
        log.info("✅ Usuario administrador creado:");
        log.info("   📧 Email: {}", adminEmail);
        log.info("   🔑 Password: admin123");
        log.info("   ⚠️  IMPORTANTE: Cambia esta contraseña en producción");
    }
    
    private void crearUsuarioJugadorPrueba() {
        String jugadorEmail = "jugador@regata.com";
        
        if (userRepository.existsByEmail(jugadorEmail)) {
            log.info("👤 Usuario jugador de prueba ya existe: {}", jugadorEmail);
            return;
        }
        
        // Crear la entidad Jugador primero
        Jugador jugadorEntidad = new Jugador("Jugador Demo", jugadorEmail);
        jugadorEntidad = jugadorRepositorio.save(jugadorEntidad);
        
        // Crear el usuario y asociarlo con el jugador
        User jugador = new User(
            "Jugador Demo",
            jugadorEmail,
            passwordEncoder.encode("jugador123"),
            Role.JUGADOR
        );
        jugador.setJugador(jugadorEntidad);
        
        userRepository.save(jugador);
        
        log.info("✅ Usuario jugador de prueba creado:");
        log.info("   📧 Email: {}", jugadorEmail);
        log.info("   🔑 Password: jugador123");
        log.info("   🎮 Entidad Jugador asociada: ID {}", jugadorEntidad.getId());
    }
}