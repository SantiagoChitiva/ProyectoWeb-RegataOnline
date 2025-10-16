package co.edu.javeriana.proyectoWeb.RegataOnline.exception_handler;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import co.edu.javeriana.proyectoWeb.RegataOnline.dto.ErrorDTO;

@ControllerAdvice
public class AppControlerAdvice {
    
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundException(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Recurso no encontrado: " + e.getMessage()));
    }
}