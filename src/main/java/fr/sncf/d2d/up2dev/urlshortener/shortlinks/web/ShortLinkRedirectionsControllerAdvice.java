package fr.sncf.d2d.up2dev.urlshortener.shortlinks.web;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.sncf.d2d.up2dev.urlshortener.common.web.ApplicationExceptionHandler;
import fr.sncf.d2d.up2dev.urlshortener.shortlinks.exceptions.ShortLinkNotFoundError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(assignableTypes = { ShortLinkRedirectionsController.class })
public class ShortLinkRedirectionsControllerAdvice {

    private final ApplicationExceptionHandler handler;

    public ShortLinkRedirectionsControllerAdvice(ApplicationExceptionHandler handler){
        this.handler = handler;
    }

    @ExceptionHandler({ ShortLinkNotFoundError.class })
    @ResponseBody
    public ResponseEntity<?> handleLinkNotFoundError(HttpServletRequest request, HttpServletResponse response, Throwable ex){
        return this.handler.handle(HttpStatus.NOT_FOUND, request, ex);
    }
}
