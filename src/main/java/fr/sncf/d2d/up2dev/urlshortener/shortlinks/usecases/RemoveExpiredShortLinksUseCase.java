package fr.sncf.d2d.up2dev.urlshortener.shortlinks.usecases;

import java.io.IOException;
import java.time.Instant;

import org.springframework.stereotype.Service;

import fr.sncf.d2d.up2dev.urlshortener.shortlinks.exceptions.ShortLinksRemovalError;
import fr.sncf.d2d.up2dev.urlshortener.shortlinks.persistence.ShortLinksRepository;

@Service
public class RemoveExpiredShortLinksUseCase {
    
    private final ShortLinksRepository repository;

    public RemoveExpiredShortLinksUseCase(ShortLinksRepository repository){
        this.repository = repository;
    }

    /**
     * Supprimer tous les liens expirés
     * @return le nombre de liens supprimés
     * @throws ShortLinksRemovalError en cas d'erreur de suppression
     */
    public long remove() throws ShortLinksRemovalError {

        final var now = Instant.now();
        try {
            return this.repository.deleteWhere(link -> link.isExpired(now));
        } catch (IOException e) {
            throw new ShortLinksRemovalError(e);
        }
    }
}
