package fr.sncf.d2d.up2dev.urlshortener.common.web;

import java.util.Optional;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Catch-all pour les erreurs qui proviennent directement du conteneur applicatif (Tomcat) */
@Controller
public class ApplicationErrorController implements ErrorController {

    private final String SERVLET_CONTAINER_STATUS_CODE_REQUEST_ATTRIBUTE = "jakarta.servlet.error.status_code";
    
    @RequestMapping("/error")
    public void handleError(HttpServletRequest request, HttpServletResponse response){
        final var status = Optional.ofNullable((Integer)request.getAttribute(SERVLET_CONTAINER_STATUS_CODE_REQUEST_ATTRIBUTE))
            .map(HttpStatus::valueOf)
            .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setStatus(status.value());
    }
}
