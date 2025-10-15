package co.edu.javeriana.proyectoWeb.RegataOnline.init;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import co.edu.javeriana.proyectoWeb.RegataOnline.model.Barco;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Celda;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Jugador;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Mapa;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Modelo;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.BarcoRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.CeldaRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.JugadorRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.MapaRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.ModeloRepositorio;


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
        List<Jugador> jugadores = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            Jugador jugador = jugadorRepositorio.save(new Jugador("Jugador " + i));
            jugadores.add(jugador);
        }
        for(int i = 0; i < 10; i++) {
            modeloRepositorio.save(new Modelo("Modelo " + i, "Color " + i));
        }  
        List<Barco> barcos = new ArrayList<>();
        int barcoIndex = 0;
        for (int j = 0; j < jugadores.size(); j++) {
            Jugador jugador = jugadores.get(j);
            List<Barco> barcosJugador = new ArrayList<>(); // Lista de barcos para este jugador

            for (int b = 0; b < 10; b++) {
                String nombreBarco = nombresBarcos[barcoIndex % nombresBarcos.length];
                Barco barco = new Barco(nombreBarco, 0, 0, barcoIndex * 5, barcoIndex * 5); // Velocidad X=0, Y=0
                barco.setJugador(jugador); // Asignar jugador al barco
                barco.setModelo(modeloRepositorio.findById(barcoIndex % 10L).orElse(null)); // Asignar modelo (esto es un ejemplo, puedes ajustarlo)
                barco = barcoRepositorio.save(barco); // Guardar el barco en la base de datos
                barcosJugador.add(barco); // Añadir el barco a la lista del jugador
                barcoIndex++;
            }

            // Ahora guardamos la relación de los barcos con el jugador
            jugador.setBarcos(barcosJugador); // Añadir los barcos al jugador
            jugadorRepositorio.save(jugador); // Guardar al jugador con su lista de barcos
        }

        // Crear el mapa de 10x10
        Mapa mapa = new Mapa(10, 10); // 10 filas, 10 columnas
        mapa = mapaRepositorio.save(mapa);

        List<Celda> celdas = new ArrayList<>();
        
        // Crear todas las celdas del mapa (10 filas x 10 columnas = 100 celdas)
        for (int fila = 0; fila < 10; fila++) {
            for (int col = 0; col < 10; col++) {
                String tipo = "";
                
                // Todo el contorno son paredes, EXCEPTO fila 9 (que solo tiene paredes en primera y última columna)
                if (fila == 0 || col == 0 || col == 9) {
                    tipo = "x";  // Pared en fila superior y columnas laterales
                }
                // Fila 9: solo las esquinas son paredes
                else if (fila == 9 && (col == 0 || col == 9)) {
                    tipo = "x";
                }
                // Celda de partida en posición (1, 1) - esquina superior izquierda interior
                else if (fila == 1 && col == 1) {
                    tipo = "P";
                }
                // Celda de meta en posición (8, 8) - esquina inferior derecha interior
                else if (fila == 8 && col == 8) {
                    tipo = "M";
                }
                // Resto son agua (navegables), incluyendo fila 9 excepto las esquinas
                
                Celda celda = new Celda(tipo, col, fila);
                celda.setMapa(mapa);
                celda = celdaRepositorio.save(celda);
                celdas.add(celda);
            }
        }

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
}