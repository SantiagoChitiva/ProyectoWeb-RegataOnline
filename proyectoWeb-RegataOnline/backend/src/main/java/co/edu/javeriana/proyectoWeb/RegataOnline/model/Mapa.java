package co.edu.javeriana.proyectoWeb.RegataOnline.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Mapa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int filas;
    private int columnas;

    @OneToMany(mappedBy = "mapa")
    private List<Celda> celdas = new ArrayList<>();

    public Mapa() {
    }

    public Mapa(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
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
    public List<Celda> getCeldas() {
        return celdas;
    }
    public void setCeldas(List<Celda> celdas) {
        this.celdas = celdas;
    }
}


