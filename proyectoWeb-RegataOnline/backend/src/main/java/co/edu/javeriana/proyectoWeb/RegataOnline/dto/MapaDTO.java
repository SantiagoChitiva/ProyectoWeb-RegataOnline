package co.edu.javeriana.proyectoWeb.RegataOnline.dto;

import java.util.List;

public class MapaDTO {
    private Long id;
    private int filas;
    private int columnas;
    private List<CeldaDTO> celdas;

    public MapaDTO() {
    }

    public MapaDTO(Long id, int filas, int columnas, List<CeldaDTO> celdas) {
        this.id = id;
        this.filas = filas;
        this.columnas = columnas;
        this.celdas = celdas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }

    public List<CeldaDTO> getCeldas() {
        return celdas;
    }

    public void setCeldas(List<CeldaDTO> celdas) {
        this.celdas = celdas;
    }
}
