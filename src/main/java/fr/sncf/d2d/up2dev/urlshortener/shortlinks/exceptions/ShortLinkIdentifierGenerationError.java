package fr.sncf.d2d.up2dev.urlshortener.shortlinks.exceptions;

/** Erreur levée si la génération d'un identifiant lié à un lien raccourci est en échec */
public class ShortLinkIdentifierGenerationError extends Exception {
    /**
     * @param src l'exception technique à l'origine de l'erreur
     */
    public ShortLinkIdentifierGenerationError(Throwable src){
        super(src);
    }
}
