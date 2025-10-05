package co.edu.javeriana.proyectoWeb.RegataOnline.dto;

public class JugadorDTO {
    private Long id;
    private String nombre;

    
    public JugadorDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }


    public JugadorDTO() {
        //TODO Auto-generated constructor stub
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    
    
}
