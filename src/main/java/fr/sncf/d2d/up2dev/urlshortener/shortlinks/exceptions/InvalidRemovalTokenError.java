package fr.sncf.d2d.up2dev.urlshortener.shortlinks.exceptions;

/** Erreur levée si un token de suppression de lien raccourci est erroné */
public class InvalidRemovalTokenError extends Exception {
    /**
     * @param provided le token invalide.
     */
    public InvalidRemovalTokenError(String provided){
        super(String.format("removal token is invalid"));
    }
}
