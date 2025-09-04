package co.edu.javeriana.proyectoWeb.RegataOnline.dto;

public class CeldaDTO {
    private Long id;
    private String tipo;
    private int posicionX;
    private int posicionY;

    public CeldaDTO() {
    }

    public CeldaDTO(Long id, String tipo, int posicionX, int posicionY) {
        this.id = id;
        this.tipo = tipo;
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public boolean esAgua() {
        return tipo == null || tipo.trim().isEmpty();
    }

    public boolean esPared() {
        return "x".equals(tipo);
    }

    public boolean esPartida() {
        return "P".equals(tipo);
    }

    public boolean esMeta() {
        return "M".equals(tipo);
    }

    public String getTipoDisplay() {
        if (esAgua()) return "Agua";
        if (esPared()) return "Pared (x)";
        if (esPartida()) return "Partida (P)";
        if (esMeta()) return "Meta (M)";
        return tipo;
    }
}