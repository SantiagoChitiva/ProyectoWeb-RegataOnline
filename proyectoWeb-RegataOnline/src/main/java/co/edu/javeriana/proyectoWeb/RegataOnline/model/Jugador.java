package co.edu.javeriana.proyectoWeb.RegataOnline.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Jugador {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;
    
    @OneToMany(mappedBy = "jugador")  
    private List<Barco> barcos = new ArrayList<>();

    public Jugador() {
    }

    public Jugador(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }   

    public String getNombre() {
        return nombre;
    }       

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }   

    public List<Barco> getBarcos() {
        return barcos;
    }
}
