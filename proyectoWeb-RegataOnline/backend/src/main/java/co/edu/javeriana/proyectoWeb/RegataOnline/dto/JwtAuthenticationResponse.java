package co.edu.javeriana.proyectoWeb.RegataOnline.dto;

import co.edu.javeriana.proyectoWeb.RegataOnline.model.Role;

public class JwtAuthenticationResponse {
    private String token;
    private String email;
    private Role role;
    private Long userId;

    public JwtAuthenticationResponse() {
    }

    public JwtAuthenticationResponse(String token, String email, Role role) {
        this.token = token;
        this.email = email;
        this.role = role;
    }

    public JwtAuthenticationResponse(String token, String email, Role role, Long userId) {
        this.token = token;
        this.email = email;
        this.role = role;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
