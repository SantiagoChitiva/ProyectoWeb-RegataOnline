package co.edu.javeriana.proyectoWeb.RegataOnline.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Celda {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Mapa mapa;

    private String tipo;
    private int posicionX;
    private int posicionY;

    @OneToMany(mappedBy = "celda")  
    private List<Barco> barcos;

    public Celda() {
    }
    public Celda(String tipo, int posicionX, int posicionY) {
        this.tipo = tipo;
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTipo() {
        return tipo;
    }  
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public int getPosicionX() {
        return posicionX;
    }
    public void setPosicionX(int posicionX) {
        this.posicionX = posicionX;
    }   
    public int getPosicionY() {
        return posicionY;
    }   
    public void setPosicionY(int posicionY) {
        this.posicionY = posicionY;
    }
    public List<Barco> getBarcos() {
        return barcos;
    }

    public void setBarcos(List<Barco> barcos) {
        this.barcos = barcos;
    }

    public Mapa getMapa() {
        return mapa;
    }

    public void setMapa(Mapa mapa) {
        this.mapa = mapa;
    }

    public boolean esAgua() {
        return tipo == null || tipo.trim().isEmpty();
    }

    public boolean esPared() {
        return "x".equals(tipo);
    }

    public boolean esPartida() {
        return "P".equals(tipo);
    }

    public boolean esMeta() {
        return "M".equals(tipo);
    }

    public boolean esNavegable() {
        return esAgua() || esPartida();
    }

    public boolean esPeligrosa() {
        return esPared();
    }
}