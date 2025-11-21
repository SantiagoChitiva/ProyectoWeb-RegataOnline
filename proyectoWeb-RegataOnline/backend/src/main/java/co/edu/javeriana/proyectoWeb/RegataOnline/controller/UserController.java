package co.edu.javeriana.proyectoWeb.RegataOnline.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.proyectoWeb.RegataOnline.model.Role;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.User;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    @Secured({ Role.Code.ADMINISTRADOR, Role.Code.JUGADOR })
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("nombre", user.getNombre());
        response.put("email", user.getEmail());
        response.put("role", user.getRole().name());
        
        if (user.getJugador() != null) {
            response.put("jugadorId", user.getJugador().getId());
            response.put("jugadorNombre", user.getJugador().getNombre());
        }
        
        return ResponseEntity.ok(response);
    }

}
