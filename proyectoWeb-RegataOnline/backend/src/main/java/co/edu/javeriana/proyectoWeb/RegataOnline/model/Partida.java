package co.edu.javeriana.proyectoWeb.RegataOnline.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Entity
public class Partida {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "jugador_id", nullable = false)
    private Jugador jugador;
    
    @ManyToOne
    @JoinColumn(name = "mapa_id", nullable = false)
    private Mapa mapa;
    
    @ManyToOne
    @JoinColumn(name = "barco_id", nullable = false)
    private Barco barco;
    
    @Column(nullable = false)
    private String estado; // "activa", "pausada", "terminada"
    
    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;
    
    @Column(name = "fecha_ultima_jugada")
    private LocalDateTime fechaUltimaJugada;
    
    @Column(name = "movimientos")
    private Integer movimientos = 0;
    
    @Column(name = "ha_llegado_meta")
    private Boolean haLlegadoMeta = false;

    public Partida() {
        this.estado = "activa";
        this.movimientos = 0;
        this.haLlegadoMeta = false;
    }

    public Partida(Jugador jugador, Mapa mapa, Barco barco) {
        this();
        this.jugador = jugador;
        this.mapa = mapa;
        this.barco = barco;
    }

    @PrePersist
    protected void onCreate() {
        this.fechaInicio = LocalDateTime.now();
        this.fechaUltimaJugada = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaUltimaJugada = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Mapa getMapa() {
        return mapa;
    }

    public void setMapa(Mapa mapa) {
        this.mapa = mapa;
    }

    public Barco getBarco() {
        return barco;
    }

    public void setBarco(Barco barco) {
        this.barco = barco;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaUltimaJugada() {
        return fechaUltimaJugada;
    }

    public void setFechaUltimaJugada(LocalDateTime fechaUltimaJugada) {
        this.fechaUltimaJugada = fechaUltimaJugada;
    }

    public Integer getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(Integer movimientos) {
        this.movimientos = movimientos;
    }

    public Boolean getHaLlegadoMeta() {
        return haLlegadoMeta;
    }

    public void setHaLlegadoMeta(Boolean haLlegadoMeta) {
        this.haLlegadoMeta = haLlegadoMeta;
    }
}
