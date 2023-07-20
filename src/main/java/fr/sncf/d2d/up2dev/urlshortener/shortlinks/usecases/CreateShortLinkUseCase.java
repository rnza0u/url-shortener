package fr.sncf.d2d.up2dev.urlshortener.shortlinks.usecases;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;

import fr.sncf.d2d.up2dev.urlshortener.shortlinks.exceptions.ShortLinkIdentifierGenerationError;
import fr.sncf.d2d.up2dev.urlshortener.shortlinks.exceptions.ShortLinkSaveError;
import fr.sncf.d2d.up2dev.urlshortener.shortlinks.exceptions.UrlValidationError;
import fr.sncf.d2d.up2dev.urlshortener.shortlinks.models.ShortLink;
import fr.sncf.d2d.up2dev.urlshortener.shortlinks.persistence.ShortLinksRepository;
import fr.sncf.d2d.up2dev.urlshortener.shortlinks.services.TokensService;
import fr.sncf.d2d.up2dev.urlshortener.shortlinks.services.UrlService;

@Service
public class CreateShortLinkUseCase {

    private final ShortLinksRepository repository;
    private final TokensService tokensService;
    private final UrlService urlService;

    public CreateShortLinkUseCase(ShortLinksRepository repository, TokensService tokensService, UrlService urlService) {
        this.repository = repository;
        this.tokensService = tokensService;
        this.urlService = urlService;
    }

    /**
     * Créer un nouveau lien raccourci
     * @param url l'URL à raccourcir
     * @return un lien raccourci
     * @throws ShortLinkIdentifierGenerationError en cas d'erreur de génération d'identifiant
     * @throws ShortLinkSaveError si le lien n'a pas pu être sauvegardé
     * @throws UrlValidationError si l'URL fournie est invalide
     */
    public ShortLink create(URL url) throws ShortLinkIdentifierGenerationError, ShortLinkSaveError, UrlValidationError {

        final var normalizedUrl = this.urlService.normalizeUrl(url);

        if (!this.urlService.isValidUrl(normalizedUrl))
            throw new UrlValidationError(url);

        final var now = Instant.now();

        final var link = ShortLink.builder()
            .id(UUID.randomUUID())
            .createdAt(Date.from(now))
            .lastAccessedAt(Date.from(now))
            .realUrl(normalizedUrl)
            .shortId(this.tokensService.generateShortId())
            .removalToken(this.tokensService.generateRemovalToken())
            .build();

        try {
            this.repository.save(link);
        } catch(IOException ex){
            throw new ShortLinkSaveError(ex);
        }

        return link;
    }
}
