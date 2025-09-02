package co.edu.javeriana.proyectoWeb.RegataOnline.dto;

public class ModeloDTO {
    private Long id;
    private String nombreModelo;
    private String color;

    public ModeloDTO() {
    }

    public ModeloDTO(Long id, String nombreModelo, String color) {
        this.id = id;
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

    public void setNombreModelo(String nombreModelo) {
        this.nombreModelo = nombreModelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }   
}
