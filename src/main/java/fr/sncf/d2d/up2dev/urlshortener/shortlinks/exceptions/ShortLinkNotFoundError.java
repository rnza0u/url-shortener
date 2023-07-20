package fr.sncf.d2d.up2dev.urlshortener.shortlinks.exceptions;

import java.util.UUID;

/**
 * Erreur levée lors qu'un lien n'a pas pu être trouvé via différents types d'identifiants.
 */
public class ShortLinkNotFoundError extends Exception {
    
    /**
     * @param shortId l'identifiant court utilisé pour chercher le lien.
    */
    public ShortLinkNotFoundError(String shortId){
        super(String.format("link with short id %s was not found", shortId));
    }

    /**
     * @param id l'identifiant technique utilisé pour chercher le lien
     */
    public ShortLinkNotFoundError(UUID id){
        super(String.format("link with id %s was not found", id));
    }
}
