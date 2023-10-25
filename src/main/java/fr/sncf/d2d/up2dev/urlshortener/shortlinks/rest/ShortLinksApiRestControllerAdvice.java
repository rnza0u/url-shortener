package fr.sncf.d2d.up2dev.urlshortener.shortlinks.rest;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import fr.sncf.d2d.up2dev.urlshortener.common.web.ApplicationExceptionHandler;
import fr.sncf.d2d.up2dev.urlshortener.shortlinks.exceptions.InvalidRemovalTokenError;
import fr.sncf.d2d.up2dev.urlshortener.shortlinks.exceptions.ShortLinkIdentifierGenerationError;
import fr.sncf.d2d.up2dev.urlshortener.shortlinks.exceptions.ShortLinkNotFoundError;
import fr.sncf.d2d.up2dev.urlshortener.shortlinks.exceptions.ShortLinkSaveError;
import fr.sncf.d2d.up2dev.urlshortener.shortlinks.exceptions.ShortLinksRemovalError;
import fr.sncf.d2d.up2dev.urlshortener.shortlinks.exceptions.UrlValidationError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = { ShortLinksApiRestController.class })
public class ShortLinksApiRestControllerAdvice {

    private static final String INVALID_URL_MESSAGE = "invalid url";

    private final ApplicationExceptionHandler handler;

    public ShortLinksApiRestControllerAdvice(ApplicationExceptionHandler handler){
        this.handler = handler;
    }
    
    @ExceptionHandler({ InvalidRemovalTokenError.class })
    public ResponseEntity<?> handleInvalidRemovalTokenError(InvalidRemovalTokenError ex, HttpServletRequest request, HttpServletResponse response){
        return this.handler.handle(HttpStatus.FORBIDDEN, request, ex);
    }

    @ExceptionHandler({ ShortLinkIdentifierGenerationError.class, ShortLinksRemovalError.class, ShortLinkSaveError.class })
    public ResponseEntity<?> handleInternatErrors(Exception ex, HttpServletRequest request, HttpServletResponse response){
        return this.handler.handle(HttpStatus.INTERNAL_SERVER_ERROR, request, ex);
    }

    @ExceptionHandler({ ShortLinkNotFoundError.class })
    public ResponseEntity<?> handleLinkNotFound(HttpServletRequest request, HttpServletResponse response, Throwable ex){
        return this.handler.handle(HttpStatus.NOT_FOUND, request, ex);
    }

    @ExceptionHandler({ UrlValidationError.class })
    public ResponseEntity<String> handleUrlValidationError(HttpServletRequest request, HttpServletResponse response, Throwable ex){
        return this.handler.handle(HttpStatus.BAD_REQUEST, INVALID_URL_MESSAGE, request, ex);
    }
}
