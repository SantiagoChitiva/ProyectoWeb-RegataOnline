package co.edu.javeriana.proyectoWeb.RegataOnline.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Barco {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne 
    private Modelo modelo;

    @ManyToOne 
    private Jugador jugador;

    @ManyToOne 
    private Celda celda;

    private int velocidadX;
    private int velocidadY;
    private int posicionX;
    private int posicionY;

    public Barco() {
    }

    public Barco(int velocidadX, int velocidadY, int posicionX, int posicionY) {
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }
    public Long getId() {
        return id;
    }               
    public void setId(Long id) {
        this.id = id;
    }   
    public int getVelocidadX() {        
        return velocidadX;
    }
    public void setVelocidadX(int velocidadX) {     
        this.velocidadX = velocidadX;
    }
    public int getVelocidadY() {
        return velocidadY;
    }   
    public void setVelocidadY(int velocidadY) {
        this.velocidadY = velocidadY;
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
    public Modelo getModelo() {
        return modelo;
    }
    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Celda getCelda() {
        return celda;
    }

    public void setCelda(Celda celda) {
        this.celda = celda;
    }
}
