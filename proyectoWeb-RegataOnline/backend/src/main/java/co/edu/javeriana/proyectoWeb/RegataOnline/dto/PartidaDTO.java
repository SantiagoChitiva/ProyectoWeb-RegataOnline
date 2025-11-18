package co.edu.javeriana.proyectoWeb.RegataOnline.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PartidaDTO {
    private Long id;
    private Long jugadorCreadorId;
    private String jugadorCreadorNombre;
    private Long mapaId;
    private Integer mapaFilas;
    private Integer mapaColumnas;
    private String estado; // "esperando", "en_curso", "terminada"
    private Integer numeroTurnoActual;
    private Integer ordenTurnoActual;
    private Integer cantidadJugadores;
    private Integer maxJugadores;
    private List<PartidaJugadorDTO> jugadores;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaUltimaJugada;
    private LocalDateTime fechaFin;
    private Long ganadorId;
    private String ganadorNombre;

    // Campos para compatibilidad con modo single player
    private Long jugadorId; // Alias de jugadorCreadorId
    private String jugadorNombre; // Alias de jugadorCreadorNombre
    private Long barcoId;
    private String barcoNombre;
    private Integer barcoPosicionX;
    private Integer barcoPosicionY;
    private Integer barcoVelocidadX;
    private Integer barcoVelocidadY;
    private Integer movimientos;
    private Boolean haLlegadoMeta;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJugadorCreadorId() {
        return jugadorCreadorId;
    }

    public void setJugadorCreadorId(Long jugadorCreadorId) {
        this.jugadorCreadorId = jugadorCreadorId;
    }

    public String getJugadorCreadorNombre() {
        return jugadorCreadorNombre;
    }

    public void setJugadorCreadorNombre(String jugadorCreadorNombre) {
        this.jugadorCreadorNombre = jugadorCreadorNombre;
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

    public List<PartidaJugadorDTO> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<PartidaJugadorDTO> jugadores) {
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

    public Long getGanadorId() {
        return ganadorId;
    }

    public void setGanadorId(Long ganadorId) {
        this.ganadorId = ganadorId;
    }

    public String getGanadorNombre() {
        return ganadorNombre;
    }

    public void setGanadorNombre(String ganadorNombre) {
        this.ganadorNombre = ganadorNombre;
    }

    // Getters y Setters para compatibilidad con single player
    public Long getJugadorId() {
        return jugadorId != null ? jugadorId : jugadorCreadorId;
    }

    public void setJugadorId(Long jugadorId) {
        this.jugadorId = jugadorId;
    }

    public String getJugadorNombre() {
        return jugadorNombre != null ? jugadorNombre : jugadorCreadorNombre;
    }

    public void setJugadorNombre(String jugadorNombre) {
        this.jugadorNombre = jugadorNombre;
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
