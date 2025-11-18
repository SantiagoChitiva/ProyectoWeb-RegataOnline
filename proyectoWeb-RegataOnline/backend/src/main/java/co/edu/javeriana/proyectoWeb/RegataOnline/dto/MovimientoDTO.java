package co.edu.javeriana.proyectoWeb.RegataOnline.dto;

public class MovimientoDTO {
    
    private Long id;
    private Long partidaJugadorId;
    private Integer numeroTurno;
    private Integer aceleracionX;
    private Integer aceleracionY;
    private Integer velocidadXAnterior;
    private Integer velocidadYAnterior;
    private Integer velocidadXNueva;
    private Integer velocidadYNueva;
    private Integer posicionXAnterior;
    private Integer posicionYAnterior;
    private Integer posicionXNueva;
    private Integer posicionYNueva;
    private Boolean llegoAMeta;
    private Boolean chocoConPared;

    public MovimientoDTO() {}

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPartidaJugadorId() {
        return partidaJugadorId;
    }

    public void setPartidaJugadorId(Long partidaJugadorId) {
        this.partidaJugadorId = partidaJugadorId;
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
}
