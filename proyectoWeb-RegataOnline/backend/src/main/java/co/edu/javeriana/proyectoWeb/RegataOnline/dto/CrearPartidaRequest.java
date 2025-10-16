package co.edu.javeriana.proyectoWeb.RegataOnline.dto;

public class CrearPartidaRequest {
    private Long jugadorId;
    private Long mapaId;
    private Long barcoId;

    // Getters y Setters
    public Long getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(Long jugadorId) {
        this.jugadorId = jugadorId;
    }

    public Long getMapaId() {
        return mapaId;
    }

    public void setMapaId(Long mapaId) {
        this.mapaId = mapaId;
    }

    public Long getBarcoId() {
        return barcoId;
    }

    public void setBarcoId(Long barcoId) {
        this.barcoId = barcoId;
    }
}
