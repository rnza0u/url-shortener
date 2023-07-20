package fr.sncf.d2d.up2dev.urlshortener.shortlinks.services;

import java.net.URI;
import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fr.sncf.d2d.up2dev.urlshortener.shortlinks.exceptions.UrlValidationError;

/** Service métier exposant des méthodes de manipulation d'URLs */
@Service
public class UrlService {

    private static final String SELF_ORIGIN_CONFIGURATION_KEY = "application.self-origin";

    private static final List<String> VALID_PROTOCOLS = List.of("http", "https");

    private final URL selfOrigin;

    public UrlService(@Value("${" + SELF_ORIGIN_CONFIGURATION_KEY + "}") String selfOrigin) throws Exception {
        final var originUri = URI.create(selfOrigin);
        this.selfOrigin = new URI(originUri.getScheme(), null, originUri.getHost(), originUri.getPort(), "/", null, null).toURL();
    }

    /**
     * Normaliser une URL.
     * @returns Un nouvel objet URL dont certains champs ont été normalisés.
     */
    public URL normalizeUrl(URL url) throws UrlValidationError {
        try {
            final var uri = url.toURI();
            return new URI(
                uri.getScheme(),
                null,
                uri.getHost(),
                uri.getPort(),
                uri.getPath(),
                uri.getQuery(),
                uri.getFragment()
            ).toURL();
        } catch (Exception ex){
            throw new UrlValidationError(url);
        }
    }

    /**
     * Vérifie si une URL est valide.
     * @param url l'URL à valider
     * @returns {@code true} si l'URL est valide
     */
    public boolean isValidUrl(URL url) {

        // vérification du protocole
        if (VALID_PROTOCOLS.stream().noneMatch(url.getProtocol()::equals))
            return false;
        
        // vérification si l'URL correspond à l'origine du serveur (pour éviter les boucles infinies de redirection).
        if (this.selfOrigin.getProtocol().equals(url.getProtocol()) && 
            this.selfOrigin.getHost().equals(url.getHost()) && 
            this.selfOrigin.getPort() == url.getPort()
        )
            return false;

        return true;
    }
}
