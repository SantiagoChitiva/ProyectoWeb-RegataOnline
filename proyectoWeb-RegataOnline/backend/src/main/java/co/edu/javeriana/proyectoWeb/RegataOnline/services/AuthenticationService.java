package co.edu.javeriana.proyectoWeb.RegataOnline.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.JwtAuthenticationResponse;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.LoginDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.dto.UserRegistrationDTO;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Jugador;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.Role;
import co.edu.javeriana.proyectoWeb.RegataOnline.model.User;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.JugadorRepositorio;
import co.edu.javeriana.proyectoWeb.RegataOnline.repository.UserRepository;

@Service
public class AuthenticationService {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JugadorRepositorio jugadorRepositorio;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signup(UserRegistrationDTO request) {
        // Determinar el rol, por defecto JUGADOR si no se especifica
        Role userRole = Role.JUGADOR;
        if (request.getRole() != null && request.getRole().equalsIgnoreCase("ADMINISTRADOR")) {
            userRole = Role.ADMINISTRADOR;
        }

        User user = new User(
                request.getNombre(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                userRole);

        // Si el rol es JUGADOR, crear automáticamente una entidad Jugador y asociarla
        if (userRole == Role.JUGADOR) {
            Jugador jugador = new Jugador(request.getNombre(), request.getEmail());
            jugador = jugadorRepositorio.save(jugador);
            
            // Asociar el jugador al usuario
            user.setJugador(jugador);
            
            log.info("Creada entidad Jugador automáticamente para el usuario: {} con email: {}", 
                user.getEmail(), request.getEmail());
        }

        // Guardar el usuario (ahora con la relación al jugador si aplica)
        userRepository.save(user);

        String jwt = jwtService.generateToken(user.getUsername());
        return new JwtAuthenticationResponse(jwt, user.getEmail(), user.getRole());
    }

    public JwtAuthenticationResponse login(LoginDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", request.getEmail());
            throw e; // Re-lanzar para que GlobalExceptionHandler la maneje
        }
        
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        String jwt = jwtService.generateToken(user.getUsername());
        return new JwtAuthenticationResponse(jwt, user.getEmail(), user.getRole());
    }

}
