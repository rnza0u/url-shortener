package fr.sncf.d2d.up2dev.urlshortener.shortlinks.usecases;

import java.net.URL;
import java.time.Instant;

import org.springframework.stereotype.Service;

import fr.sncf.d2d.up2dev.urlshortener.shortlinks.exceptions.ShortLinkNotFoundError;
import fr.sncf.d2d.up2dev.urlshortener.shortlinks.persistence.ShortLinksRepository;

@Service
public class AccessShortLinkUseCase {
    
    private final ShortLinksRepository repository;

    public AccessShortLinkUseCase(ShortLinksRepository repository){
        this.repository = repository;
    }

    /**
     * Accéder à une URL via son lien raccourci.
     * @param shortId L'identifiant court du lien raccourci.
     * @return L'URL réelle.
     * @throws ShortLinkNotFoundError si le lien raccourci n'existe pas.
     */
    public URL access(String shortId) throws ShortLinkNotFoundError {

        assert shortId != null;

        return this.repository.findByShortId(shortId)
            .map(link -> link.access(Instant.now()))
            .orElseThrow(() -> new ShortLinkNotFoundError(shortId));
    }
}
