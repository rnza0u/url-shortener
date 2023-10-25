package fr.sncf.d2d.up2dev.urlshortener.common.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Ici on centralise la gestion des erreurs (journalisation etc...) et la création de la réponse HTTP d'erreur.
 */
@Component
public class ApplicationExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Traite l'erreur et renvoie la réponse HTTP associée.
     * @param status le statut HTTP voulu
     * @param request l'objet requête correspondant à la levée d'exception
     * @param error l'objet symbolisant l'erreur
     * @return la réponse HTTP sous forme de {@link ResponseEntity}.
     */
    public ResponseEntity<?> handle(HttpStatus status, HttpServletRequest request, Throwable error){
        this.log(request, error);
        return ResponseEntity.status(status).build();
    }

    /**
     * Similaire à {@link #handle(HttpStatus, HttpServletRequest, Throwable)}, mais avec un corps de réponse qui sera sérialisé en JSON.
     */
    public <T> ResponseEntity<T> handle(HttpStatus status, T body, HttpServletRequest request, Throwable error){
        this.log(request, error);
        return ResponseEntity.status(status).body(body);
    }

    private void log(HttpServletRequest request, Throwable error){

        final var trace = error.getStackTrace();
        final var source = trace[0];

        this.logger.error(
            "{} {} from {}, {}: {} ({} => line {})",
            request.getMethod(),
            request.getServletPath(), 
            request.getRemoteAddr(),
            error.getClass().getSimpleName(),
            error.getMessage(),
            source.getFileName(),
            source.getLineNumber()
        );
    }
    
}
