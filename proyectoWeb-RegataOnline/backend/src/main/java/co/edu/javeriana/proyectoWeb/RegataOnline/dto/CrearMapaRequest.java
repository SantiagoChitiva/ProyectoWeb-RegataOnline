package co.edu.javeriana.proyectoWeb.RegataOnline.dto;

import java.util.List;

public class CrearMapaRequest {
    private int filas;
    private int columnas;
    private List<CeldaDTO> celdasSeleccionadas;

    public CrearMapaRequest() {
    }

    public CrearMapaRequest(int filas, int columnas, List<CeldaDTO> celdasSeleccionadas) {
        this.filas = filas;
        this.columnas = columnas;
        this.celdasSeleccionadas = celdasSeleccionadas;
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

    public List<CeldaDTO> getCeldasSeleccionadas() {
        return celdasSeleccionadas;
    }

    public void setCeldasSeleccionadas(List<CeldaDTO> celdasSeleccionadas) {
        this.celdasSeleccionadas = celdasSeleccionadas;
    }
}
