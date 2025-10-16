package co.edu.javeriana.proyectoWeb.RegataOnline.dto;

import java.time.LocalDateTime;

public class PartidaDTO {
    private Long id;
    private Long jugadorId;
    private String jugadorNombre;
    private Long mapaId;
    private Integer mapaFilas;
    private Integer mapaColumnas;
    private Long barcoId;
    private String barcoNombre;
    private Integer barcoPosicionX;
    private Integer barcoPosicionY;
    private String estado;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaUltimaJugada;
    private Integer movimientos;
    private Boolean haLlegadoMeta;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(Long jugadorId) {
        this.jugadorId = jugadorId;
    }

    public String getJugadorNombre() {
        return jugadorNombre;
    }

    public void setJugadorNombre(String jugadorNombre) {
        this.jugadorNombre = jugadorNombre;
    }

    public Long getMapaId() {
        return mapaId;
    }

    public void setMapaId(Long mapaId) {
        this.mapaId = mapaId;
    }

    public Integer getMapaFilas() {
        return mapaFilas;
    }

    public void setMapaFilas(Integer mapaFilas) {
        this.mapaFilas = mapaFilas;
    }

    public Integer getMapaColumnas() {
        return mapaColumnas;
    }

    public void setMapaColumnas(Integer mapaColumnas) {
        this.mapaColumnas = mapaColumnas;
    }

    public Long getBarcoId() {
        return barcoId;
    }

    public void setBarcoId(Long barcoId) {
        this.barcoId = barcoId;
    }

    public String getBarcoNombre() {
        return barcoNombre;
    }

    public void setBarcoNombre(String barcoNombre) {
        this.barcoNombre = barcoNombre;
    }

    public Integer getBarcoPosicionX() {
        return barcoPosicionX;
    }

    public void setBarcoPosicionX(Integer barcoPosicionX) {
        this.barcoPosicionX = barcoPosicionX;
    }

    public Integer getBarcoPosicionY() {
        return barcoPosicionY;
    }

    public void setBarcoPosicionY(Integer barcoPosicionY) {
        this.barcoPosicionY = barcoPosicionY;
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
