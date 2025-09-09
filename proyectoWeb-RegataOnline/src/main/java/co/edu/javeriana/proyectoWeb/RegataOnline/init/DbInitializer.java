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
                Barco barco = new Barco(barcoIndex, barcoIndex + 1, barcoIndex * 5, barcoIndex * 5);
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

        // Crear el mapa
        Mapa mapa = mapaRepositorio.save(new Mapa(15, 10));

        List<Celda> celdas = new ArrayList<>();
        
        // Crear más celdas de agua (tu código original solo creaba 15)
        for (int i = 0; i < 100; i++) {
            Celda celda = new Celda("", i % 15, i / 15); // tipo vacío = agua
            celda = celdaRepositorio.save(celda);
            celdas.add(celda);
        }

        // Crear algunas paredes (x) - exactamente como tu código
        for (int i = 0; i < 3; i++) {
            Celda celda = new Celda("x", i + 15, 2);
            celda = celdaRepositorio.save(celda);
            celdas.add(celda);
        }

        // Crear celdas de partida (P) - exactamente como tu código
        for (int i = 0; i < 3; i++) {
            Celda celda = new Celda("P", i, 0);
            celda = celdaRepositorio.save(celda);
            celdas.add(celda);
        }

        // Crear celdas de meta (M) - exactamente como tu código
        for (int i = 0; i < 3; i++) {
            Celda celda = new Celda("M", i, 5);
            celda = celdaRepositorio.save(celda);
            celdas.add(celda);
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