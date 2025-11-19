package co.edu.javeriana.proyectoWeb.RegataOnline.dto;

public class PartidaJugadorDTO {
    
    private Long id;
    private Long partidaId;
    private Long jugadorId;
    private String jugadorNombre;
    private Long barcoId;
    private String barcoNombre;
    private String barcoColor;
    private Integer ordenTurno;
    private Integer posicionX;
    private Integer posicionY;
    private Integer velocidadX;
    private Integer velocidadY;
    private String estado;
    private Boolean haLlegadoMeta;
    private Integer posicionFinal;
    private Integer movimientosRealizados;

    public PartidaJugadorDTO() {}

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPartidaId() {
        return partidaId;
    }

    public void setPartidaId(Long partidaId) {
        this.partidaId = partidaId;
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

    public String getBarcoColor() {
        return barcoColor;
    }

    public void setBarcoColor(String barcoColor) {
        this.barcoColor = barcoColor;
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

    public Integer getMovimientosRealizados() {
        return movimientosRealizados;
    }

    public void setMovimientosRealizados(Integer movimientosRealizados) {
        this.movimientosRealizados = movimientosRealizados;
    }
}
