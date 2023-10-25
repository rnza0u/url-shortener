package fr.sncf.d2d.up2dev.urlshortener.common.web;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.core.JacksonException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ControllerAdvice global pour toute l'application.
 */
@ControllerAdvice
/** ce ControllerAdvice est non prioritaire. */
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalControllerAdvice {

    private final ApplicationExceptionHandler handler;

    public GlobalControllerAdvice(ApplicationExceptionHandler handler){
        this.handler = handler;
    }

    /** Gestion des erreurs de Jackson ou de conversion de message sur HTTP */
    @ExceptionHandler({ JacksonException.class, HttpMessageConversionException.class })
    public ResponseEntity<?> badRequest(Exception exception, HttpServletRequest request, HttpServletResponse response){
        return this.handler.handle(HttpStatus.BAD_REQUEST, request, exception);
    }
    
    /* Un "catch-all" pour toutes les erreurs non gérées. */
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<?> catchAll(Exception exception, HttpServletRequest request, HttpServletResponse response){
        return this.handler.handle(HttpStatus.INTERNAL_SERVER_ERROR, request, exception);
    }
}

