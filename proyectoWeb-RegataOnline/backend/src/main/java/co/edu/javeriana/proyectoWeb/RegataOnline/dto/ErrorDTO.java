package co.edu.javeriana.proyectoWeb.RegataOnline.dto;

public class ErrorDTO {
    private String errorString;

    public ErrorDTO(String errorString) {
        this.errorString = errorString;
    }

    public String getErrorString() {
        return errorString;
    }
}
