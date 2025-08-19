package co.edu.javeriana.proyectoWeb.RegataOnline.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import co.edu.javeriana.proyectoWeb.RegataOnline.model.ModeloBarco;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Barco;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Jugador;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Celda;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Mapa;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.BarcoRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.CeldaRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.JugadorRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.MapaRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.ModeloBarcoRepositorio;

import java.util.ArrayList;
import java.util.List;


@Component
public class DbInitializer implements CommandLineRunner {

    @Autowired
    private ModeloBarcoRepositorio modeloBarcoRepositorio;

    @Autowired
    private BarcoRepositorio barcoRepositorio;

    @Autowired
    private JugadorRepositorio jugadorRepositorio;

    @Autowired
    private MapaRepositorio mapaRepositorio;
    
    @Autowired
    private CeldaRepositorio celdaRepositorio;

    @Override
    public void run(String... args) throws Exception {
        List<Jugador> jugadores = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            Jugador jugador = jugadorRepositorio.save(new Jugador("Jugador " + i));
            jugadores.add(jugador);
        }
        for(int i = 0; i < 10; i++) {
            modeloBarcoRepositorio.save(new ModeloBarco("Modelo " + i, "Color " + i));
        }  
        List<Barco> barcos = new ArrayList<>();
        for(int i = 0; i < 50; i++) {
            Barco barco = barcoRepositorio.save(new Barco(i, i + 1, i * 5, i * 5));
            barcos.add(barco);
        }
    }

}

