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
    private Integer barcoVelocidadX;
    private Integer barcoVelocidadY;
    private String estado;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaUltimaJugada;
    private Integer movimientos;
    private Boolean haLlegadoMeta;

    public PartidaDTO() {
    }

    public PartidaDTO(Long id, Long jugadorId, String jugadorNombre, Long mapaId, Integer mapaFilas,
            Integer mapaColumnas, Long barcoId, String barcoNombre, Integer barcoPosicionX, Integer barcoPosicionY,
            Integer barcoVelocidadX, Integer barcoVelocidadY, String estado, LocalDateTime fechaInicio,
            LocalDateTime fechaUltimaJugada, Integer movimientos, Boolean haLlegadoMeta) {
        this.id = id;
        this.jugadorId = jugadorId;
        this.jugadorNombre = jugadorNombre;
        this.mapaId = mapaId;
        this.mapaFilas = mapaFilas;
        this.mapaColumnas = mapaColumnas;
        this.barcoId = barcoId;
        this.barcoNombre = barcoNombre;
        this.barcoPosicionX = barcoPosicionX;
        this.barcoPosicionY = barcoPosicionY;
        this.barcoVelocidadX = barcoVelocidadX;
        this.barcoVelocidadY = barcoVelocidadY;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaUltimaJugada = fechaUltimaJugada;
        this.movimientos = movimientos;
        this.haLlegadoMeta = haLlegadoMeta;
    }

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

    public Integer getBarcoVelocidadX() {
        return barcoVelocidadX;
    }

    public void setBarcoVelocidadX(Integer barcoVelocidadX) {
        this.barcoVelocidadX = barcoVelocidadX;
    }

    public Integer getBarcoVelocidadY() {
        return barcoVelocidadY;
    }

    public void setBarcoVelocidadY(Integer barcoVelocidadY) {
        this.barcoVelocidadY = barcoVelocidadY;
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
