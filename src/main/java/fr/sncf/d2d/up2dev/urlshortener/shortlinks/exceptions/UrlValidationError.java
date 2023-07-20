package fr.sncf.d2d.up2dev.urlshortener.shortlinks.exceptions;

import java.net.URL;

/**
 * Erreur levée lors qu'une URL de lien raccourcie est invalide.
 */
public class UrlValidationError extends Exception {
    /**
     * @param url l'URL invalide.
     */
    public UrlValidationError(URL url){
        super(String.format("url %s is not valid", url.toString()));
    }
}
