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
@Table(name = "movimiento")
public class Movimiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "partida_jugador_id", nullable = false)
    private PartidaJugador partidaJugador;
    
    @Column(name = "numero_turno", nullable = false)
    private Integer numeroTurno; // Turno global de la partida (1, 2, 3...)
    
    @Column(name = "aceleracion_x", nullable = false)
    private Integer aceleracionX; // -1, 0, +1
    
    @Column(name = "aceleracion_y", nullable = false)
    private Integer aceleracionY; // -1, 0, +1
    
    @Column(name = "velocidad_x_anterior", nullable = false)
    private Integer velocidadXAnterior;
    
    @Column(name = "velocidad_y_anterior", nullable = false)
    private Integer velocidadYAnterior;
    
    @Column(name = "velocidad_x_nueva", nullable = false)
    private Integer velocidadXNueva;
    
    @Column(name = "velocidad_y_nueva", nullable = false)
    private Integer velocidadYNueva;
    
    @Column(name = "posicion_x_anterior", nullable = false)
    private Integer posicionXAnterior;
    
    @Column(name = "posicion_y_anterior", nullable = false)
    private Integer posicionYAnterior;
    
    @Column(name = "posicion_x_nueva", nullable = false)
    private Integer posicionXNueva;
    
    @Column(name = "posicion_y_nueva", nullable = false)
    private Integer posicionYNueva;
    
    @Column(name = "llego_a_meta")
    private Boolean llegoAMeta = false;
    
    @Column(name = "choco_con_pared")
    private Boolean chocoConPared = false;
    
    @Column(name = "fecha_movimiento")
    private LocalDateTime fechaMovimiento;

    public Movimiento() {
        this.llegoAMeta = false;
        this.chocoConPared = false;
    }

    @PrePersist
    protected void onCreate() {
        this.fechaMovimiento = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PartidaJugador getPartidaJugador() {
        return partidaJugador;
    }

    public void setPartidaJugador(PartidaJugador partidaJugador) {
        this.partidaJugador = partidaJugador;
    }

    public Integer getNumeroTurno() {
        return numeroTurno;
    }

    public void setNumeroTurno(Integer numeroTurno) {
        this.numeroTurno = numeroTurno;
    }

    public Integer getAceleracionX() {
        return aceleracionX;
    }

    public void setAceleracionX(Integer aceleracionX) {
        this.aceleracionX = aceleracionX;
    }

    public Integer getAceleracionY() {
        return aceleracionY;
    }

    public void setAceleracionY(Integer aceleracionY) {
        this.aceleracionY = aceleracionY;
    }

    public Integer getVelocidadXAnterior() {
        return velocidadXAnterior;
    }

    public void setVelocidadXAnterior(Integer velocidadXAnterior) {
        this.velocidadXAnterior = velocidadXAnterior;
    }

    public Integer getVelocidadYAnterior() {
        return velocidadYAnterior;
    }

    public void setVelocidadYAnterior(Integer velocidadYAnterior) {
        this.velocidadYAnterior = velocidadYAnterior;
    }

    public Integer getVelocidadXNueva() {
        return velocidadXNueva;
    }

    public void setVelocidadXNueva(Integer velocidadXNueva) {
        this.velocidadXNueva = velocidadXNueva;
    }

    public Integer getVelocidadYNueva() {
        return velocidadYNueva;
    }

    public void setVelocidadYNueva(Integer velocidadYNueva) {
        this.velocidadYNueva = velocidadYNueva;
    }

    public Integer getPosicionXAnterior() {
        return posicionXAnterior;
    }

    public void setPosicionXAnterior(Integer posicionXAnterior) {
        this.posicionXAnterior = posicionXAnterior;
    }

    public Integer getPosicionYAnterior() {
        return posicionYAnterior;
    }

    public void setPosicionYAnterior(Integer posicionYAnterior) {
        this.posicionYAnterior = posicionYAnterior;
    }

    public Integer getPosicionXNueva() {
        return posicionXNueva;
    }

    public void setPosicionXNueva(Integer posicionXNueva) {
        this.posicionXNueva = posicionXNueva;
    }

    public Integer getPosicionYNueva() {
        return posicionYNueva;
    }

    public void setPosicionYNueva(Integer posicionYNueva) {
        this.posicionYNueva = posicionYNueva;
    }

    public Boolean getLlegoAMeta() {
        return llegoAMeta;
    }

    public void setLlegoAMeta(Boolean llegoAMeta) {
        this.llegoAMeta = llegoAMeta;
    }

    public Boolean getChocoConPared() {
        return chocoConPared;
    }

    public void setChocoConPared(Boolean chocoConPared) {
        this.chocoConPared = chocoConPared;
    }

    public LocalDateTime getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(LocalDateTime fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }
}
