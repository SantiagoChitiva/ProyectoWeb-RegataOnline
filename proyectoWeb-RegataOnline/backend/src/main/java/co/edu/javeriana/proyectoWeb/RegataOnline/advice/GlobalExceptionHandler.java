package co.edu.javeriana.bancosec.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = { AuthenticationException.class })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String deniedPermissionException(AuthenticationException e) {
        log.error("Unauthorized:", e);
        return "Invalid credentials";
    }

    @ExceptionHandler(value = { AccessDeniedException.class })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String internalServerError(AccessDeniedException e) {
        log.error("Forbidden:", e);
        return "User has no access to resource";
    }

    @ExceptionHandler(value = { Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String internalServerError(Exception e) {
        log.error("Exception:", e);
        return "Internal error";
    }
}
