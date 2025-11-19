package co.edu.javeriana.proyectoWeb.RegataOnline.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Entity
public class Partida {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "jugador_creador_id", nullable = false)
    private Jugador jugadorCreador; // Jugador que creó la partida
    
    @ManyToOne
    @JoinColumn(name = "mapa_id", nullable = false)
    private Mapa mapa;
    
    @Column(nullable = false)
    private String estado; // "esperando", "en_curso", "terminada"
    
    @Column(name = "numero_turno_actual", nullable = false)
    private Integer numeroTurnoActual = 1; // Turno global de la partida
    
    @Column(name = "orden_turno_actual", nullable = false)
    private Integer ordenTurnoActual = 1; // Qué jugador debe jugar (1-4)
    
    @Column(name = "cantidad_jugadores", nullable = false)
    private Integer cantidadJugadores = 0; // Cuántos jugadores están en la partida
    
    @Column(name = "max_jugadores", nullable = false)
    private Integer maxJugadores = 4; // Máximo 4 jugadores
    
    @OneToMany(mappedBy = "partida", cascade = CascadeType.ALL)
    private List<PartidaJugador> jugadores = new ArrayList<>();
    
    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;
    
    @Column(name = "fecha_ultima_jugada")
    private LocalDateTime fechaUltimaJugada;
    
    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;
    
    @ManyToOne
    @JoinColumn(name = "ganador_id")
    private Jugador ganador; // Jugador que llegó primero a la meta

    public Partida() {
        this.estado = "esperando";
        this.numeroTurnoActual = 1;
        this.ordenTurnoActual = 1;
        this.cantidadJugadores = 0;
        this.maxJugadores = 4;
        this.jugadores = new ArrayList<>();
    }

    public Partida(Jugador jugadorCreador, Mapa mapa) {
        this();
        this.jugadorCreador = jugadorCreador;
        this.mapa = mapa;
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

    public Jugador getJugadorCreador() {
        return jugadorCreador;
    }

    public void setJugadorCreador(Jugador jugadorCreador) {
        this.jugadorCreador = jugadorCreador;
    }

    public Mapa getMapa() {
        return mapa;
    }

    public void setMapa(Mapa mapa) {
        this.mapa = mapa;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getNumeroTurnoActual() {
        return numeroTurnoActual;
    }

    public void setNumeroTurnoActual(Integer numeroTurnoActual) {
        this.numeroTurnoActual = numeroTurnoActual;
    }

    public Integer getOrdenTurnoActual() {
        return ordenTurnoActual;
    }

    public void setOrdenTurnoActual(Integer ordenTurnoActual) {
        this.ordenTurnoActual = ordenTurnoActual;
    }

    public Integer getCantidadJugadores() {
        return cantidadJugadores;
    }

    public void setCantidadJugadores(Integer cantidadJugadores) {
        this.cantidadJugadores = cantidadJugadores;
    }

    public Integer getMaxJugadores() {
        return maxJugadores;
    }

    public void setMaxJugadores(Integer maxJugadores) {
        this.maxJugadores = maxJugadores;
    }

    public List<PartidaJugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<PartidaJugador> jugadores) {
        this.jugadores = jugadores;
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

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Jugador getGanador() {
        return ganador;
    }

    public void setGanador(Jugador ganador) {
        this.ganador = ganador;
    }
}
