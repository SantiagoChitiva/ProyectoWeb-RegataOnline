package co.edu.javeriana.proyectoWeb.RegataOnline.dto;


public class RealizarMovimientoRequest {
    
    private Long partidaId;
    private Long jugadorId;
    private Integer aceleracionX; // -1, 0, +1
    private Integer aceleracionY; // -1, 0, +1

    public RealizarMovimientoRequest() {}

    // Getters y Setters
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
}
