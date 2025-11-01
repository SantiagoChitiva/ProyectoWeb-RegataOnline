package co.edu.javeriana.proyectoWeb.RegataOnline.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    JUGADOR(Code.JUGADOR),
    ADMINISTRADOR(Code.ADMINISTRADOR);

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public class Code {
        public static final String ADMINISTRADOR = "ADMINISTRADOR";
        public static final String JUGADOR = "JUGADOR";
    }
}
