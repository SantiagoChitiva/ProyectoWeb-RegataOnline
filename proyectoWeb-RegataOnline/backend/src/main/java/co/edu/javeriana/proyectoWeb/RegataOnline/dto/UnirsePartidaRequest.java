package co.edu.javeriana.proyectoWeb.RegataOnline.dto;

import java.util.List;


public class UnirsePartidaRequest {
    
    private Long partidaId;
    private Long jugadorId;
    private Long barcoId;

    public UnirsePartidaRequest() {}

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

    public Long getBarcoId() {
        return barcoId;
    }

    public void setBarcoId(Long barcoId) {
        this.barcoId = barcoId;
    }
}
