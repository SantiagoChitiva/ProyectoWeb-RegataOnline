package co.edu.javeriana.proyectoWeb.RegataOnline.dto;

import java.util.List;

public class BarcoJugadorDTO {
    private Long jugadorId;
    private List<Long> barcosIds;


    public BarcoJugadorDTO(Long jugadorId, List<Long> barcosIds) {
        this.jugadorId = jugadorId;
        this.barcosIds = barcosIds;
    }


    public Long getJugadorId() {
        return jugadorId;
    }


    public void setJugadorId(Long jugadorId) {
        this.jugadorId = jugadorId;
    }


    public List<Long> getBarcosIds() {
        return barcosIds;
    }


    public void setBarcosIds(List<Long> barcosIds) {
        this.barcosIds = barcosIds;
    }

    

    

}
