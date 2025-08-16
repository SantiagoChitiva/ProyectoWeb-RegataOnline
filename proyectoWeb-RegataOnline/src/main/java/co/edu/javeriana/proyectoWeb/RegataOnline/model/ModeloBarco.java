package co.edu.javeriana.proyectoWeb.RegataOnline.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ModeloBarco {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombreModelo;
    private String color;

    public ModeloBarco() {
    }
    public ModeloBarco(String nombreModelo, String color) {
        this.nombreModelo = nombreModelo;
        this.color = color;
    }   
    public Long getId() {
        return id;
    }  
    public void setId(Long id) {
        this.id = id;
    }   
    public String getNombreModelo() {
        return nombreModelo;
    }   
    public void setNombreModelo(String nombreModelo){
        this.nombreModelo = nombreModelo;
    }
    public String getColor() {
        return color;
    }   
    public void setColor(String color) {
        this.color = color;
    }   
}
