package co.edu.javeriana.proyectoWeb.RegataOnline.exception_handler;
import java.util.NoSuchElementException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class AppControlerAdvice {
    
    @ExceptionHandler(NoSuchElementException.class)
    public ModelAndView handleNotFoundException(NoSuchElementException e) {
        ModelAndView modelAndView = new ModelAndView("error-page");
        modelAndView.addObject("exceptionText", e.toString());
        return modelAndView;
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ModelAndView handleNoResourceFoundException(NoResourceFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error-page");
        modelAndView.addObject("exceptionText" + e.toString());
        return modelAndView;
    }
}