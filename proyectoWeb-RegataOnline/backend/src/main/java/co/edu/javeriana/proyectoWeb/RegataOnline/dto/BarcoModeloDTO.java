package co.edu.javeriana.proyectoWeb.RegataOnline.dto;

import java.util.List;

public class BarcoModeloDTO {
    private Long modeloId;
    private List<Long> barcosIds;

    public BarcoModeloDTO() {
    }

    public BarcoModeloDTO(Long modeloId, List<Long> barcosIds) {
        this.modeloId = modeloId;
        this.barcosIds = barcosIds;
    }

    public Long getModeloId() {
        return modeloId;
    }

    public void setModeloId(Long modeloId) {
        this.modeloId = modeloId;
    }

    public List<Long> getBarcosIds() {
        return barcosIds;
    }

    public void setBarcosIds(List<Long> barcosIds) {
        this.barcosIds = barcosIds;
    }

    

    

    
}