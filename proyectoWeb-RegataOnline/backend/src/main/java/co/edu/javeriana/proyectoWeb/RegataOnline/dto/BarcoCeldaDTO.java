package co.edu.javeriana.proyectoWeb.RegataOnline.dto;

import java.util.List;

public class BarcoCeldaDTO {
    private Long celdaId;
    private List<Long> barcosIds;

    public BarcoCeldaDTO() {
    }

    public BarcoCeldaDTO(Long celdaId, List<Long> barcosIds) {
        this.celdaId = celdaId;
        this.barcosIds = barcosIds;
    }

    public Long getCeldaId() {
        return celdaId;
    }

    public void setCeldaId(Long celdaId) {
        this.celdaId = celdaId;
    }

    public List<Long> getBarcosIds() {
        return barcosIds;
    }

    public void setBarcosIds(List<Long> barcosIds) {
        this.barcosIds = barcosIds;
    }
}