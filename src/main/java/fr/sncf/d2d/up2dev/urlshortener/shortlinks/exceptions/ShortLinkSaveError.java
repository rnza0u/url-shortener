package fr.sncf.d2d.up2dev.urlshortener.shortlinks.exceptions;

/**
 * Erreur levée lorsqu'une erreur technique survient à la sauvegarde d'un lien.
 */
public class ShortLinkSaveError extends Exception {
    /**
     * @param src l'exception technique à l'origine de l'erreur.
     */
    public ShortLinkSaveError(Throwable src){
        super(src);
    }
}
