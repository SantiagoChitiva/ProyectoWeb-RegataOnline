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
import jakarta.persistence.Table;


@Entity
@Table(name = "partida_jugador")
public class PartidaJugador {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "partida_id", nullable = false)
    private Partida partida;
    
    @ManyToOne
    @JoinColumn(name = "jugador_id", nullable = false)
    private Jugador jugador;
    
    @ManyToOne
    @JoinColumn(name = "barco_id", nullable = false)
    private Barco barco;
    
    @Column(name = "orden_turno", nullable = false)
    private Integer ordenTurno; // 1, 2, 3, 4 (orden en el que juegan)
    
    @Column(name = "posicion_x", nullable = false)
    private Integer posicionX;
    
    @Column(name = "posicion_y", nullable = false)
    private Integer posicionY;
    
    @Column(name = "velocidad_x", nullable = false)
    private Integer velocidadX = 0;
    
    @Column(name = "velocidad_y", nullable = false)
    private Integer velocidadY = 0;
    
    @Column(nullable = false)
    private String estado; // "jugando", "terminado", "abandonado"
    
    @Column(name = "ha_llegado_meta")
    private Boolean haLlegadoMeta = false;
    
    @Column(name = "posicion_final")
    private Integer posicionFinal; // 1º, 2º, 3º, 4º
    
    @Column(name = "fecha_union")
    private LocalDateTime fechaUnion;
    
    @Column(name = "movimientos_realizados")
    private Integer movimientosRealizados = 0;

    public PartidaJugador() {
        this.estado = "jugando";
        this.haLlegadoMeta = false;
        this.velocidadX = 0;
        this.velocidadY = 0;
        this.movimientosRealizados = 0;
    }

    public PartidaJugador(Partida partida, Jugador jugador, Barco barco, Integer ordenTurno, Integer posicionX, Integer posicionY) {
        this();
        this.partida = partida;
        this.jugador = jugador;
        this.barco = barco;
        this.ordenTurno = ordenTurno;
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }

    @PrePersist
    protected void onCreate() {
        this.fechaUnion = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Barco getBarco() {
        return barco;
    }

    public void setBarco(Barco barco) {
        this.barco = barco;
    }

    public Integer getOrdenTurno() {
        return ordenTurno;
    }

    public void setOrdenTurno(Integer ordenTurno) {
        this.ordenTurno = ordenTurno;
    }

    public Integer getPosicionX() {
        return posicionX;
    }

    public void setPosicionX(Integer posicionX) {
        this.posicionX = posicionX;
    }

    public Integer getPosicionY() {
        return posicionY;
    }

    public void setPosicionY(Integer posicionY) {
        this.posicionY = posicionY;
    }

    public Integer getVelocidadX() {
        return velocidadX;
    }

    public void setVelocidadX(Integer velocidadX) {
        this.velocidadX = velocidadX;
    }

    public Integer getVelocidadY() {
        return velocidadY;
    }

    public void setVelocidadY(Integer velocidadY) {
        this.velocidadY = velocidadY;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Boolean getHaLlegadoMeta() {
        return haLlegadoMeta;
    }

    public void setHaLlegadoMeta(Boolean haLlegadoMeta) {
        this.haLlegadoMeta = haLlegadoMeta;
    }

    public Integer getPosicionFinal() {
        return posicionFinal;
    }

    public void setPosicionFinal(Integer posicionFinal) {
        this.posicionFinal = posicionFinal;
    }

    public LocalDateTime getFechaUnion() {
        return fechaUnion;
    }

    public void setFechaUnion(LocalDateTime fechaUnion) {
        this.fechaUnion = fechaUnion;
    }

    public Integer getMovimientosRealizados() {
        return movimientosRealizados;
    }

    public void setMovimientosRealizados(Integer movimientosRealizados) {
        this.movimientosRealizados = movimientosRealizados;
    }
}
