package co.edu.javeriana.proyectoWeb.RegataOnline.dto;

public class BarcoDTO {
    private Long id;
    private String nombre;
    private int velocidadX;
    private int velocidadY;
    private int posicionX;
    private int posicionY;
    private Long modeloId;
    private Long jugadorId;
    private Long celdaId;
    private String nombreModeloBarco;
    private String nombreJugador;
    private String tipoCelda;
    
    public BarcoDTO() {
        //TODO Auto-generated constructor stub
    }

    public BarcoDTO(Long id, String nombre, int velocidadX, int velocidadY, int posicionX, int posicionY) {
        this.id = id;
        this.nombre = nombre;
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
        this.posicionX = posicionX;
        this.posicionY = posicionY;
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

    public int getVelocidadX() {
        return velocidadX;
    }

    public void setVelocidadX(int velocidadX) {
        this.velocidadX = velocidadX;
    }

    public int getVelocidadY() {
        return velocidadY;
    }

    public void setVelocidadY(int velocidadY) {
        this.velocidadY = velocidadY;
    }

    public int getPosicionX() {
        return posicionX;
    }

    public void setPosicionX(int posicionX) {
        this.posicionX = posicionX;
    }

    public int getPosicionY() {
        return posicionY;
    }

    public void setPosicionY(int posicionY) {
        this.posicionY = posicionY;
    }

    public Long getModeloId() {
        return modeloId;
    }

    public void setModeloId(Long modeloId) {
        this.modeloId = modeloId;
    }

    public Long getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(Long jugadorId) {
        this.jugadorId = jugadorId;
    }

    public Long getCeldaId() {
        return celdaId;
    }

    public void setCeldaId(Long celdaId) {
        this.celdaId = celdaId;
    }

    public String getNombreModeloBarco() {
        return nombreModeloBarco;
    }

    public void setNombreModeloBarco(String nombreModeloBarco) {
        this.nombreModeloBarco = nombreModeloBarco;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }
    
    public String getTipoCelda() {
        return tipoCelda;
    }

    public void setTipoCelda(String tipoCelda) {
        this.tipoCelda = tipoCelda;
    }
}
