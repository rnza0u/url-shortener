package fr.sncf.d2d.up2dev.urlshortener.shortlinks.usecases;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import fr.sncf.d2d.up2dev.urlshortener.shortlinks.exceptions.InvalidRemovalTokenError;
import fr.sncf.d2d.up2dev.urlshortener.shortlinks.exceptions.ShortLinkNotFoundError;
import fr.sncf.d2d.up2dev.urlshortener.shortlinks.exceptions.ShortLinksRemovalError;
import fr.sncf.d2d.up2dev.urlshortener.shortlinks.persistence.ShortLinksRepository;
import fr.sncf.d2d.up2dev.urlshortener.shortlinks.services.TokensService;

@Service
public class DeleteShortLinkUseCase {

    private final ShortLinksRepository repository;
    private final TokensService tokensService;

    public DeleteShortLinkUseCase(ShortLinksRepository repository, TokensService tokensService){
        this.repository = repository;
        this.tokensService = tokensService;
    }
    
    /**
     * Supprimer un lien raccourci
     * @param id l'identifiant technique du lien raccourci
     * @param removalToken le token de sécurité fourni à la création du lien
     * @throws ShortLinkNotFoundError le lien n'existe pas
     * @throws InvalidRemovalTokenError le token de sécurité n'est pas valide
     * @throws ShortLinksRemovalError le lien n'a pas pu être supprimé
     */
    public void delete(UUID id, String removalToken) throws ShortLinkNotFoundError, InvalidRemovalTokenError, ShortLinksRemovalError {
        
        final var link = this.repository.findById(id)
            .orElseThrow(() -> new ShortLinkNotFoundError(id));

        if (!this.tokensService.isRemovalTokenValid(link.getRemovalToken(), removalToken))
            throw new InvalidRemovalTokenError(removalToken);

        try {
            this.repository.deleteWhere(l -> link.getId().equals(l.getId()));
        } catch(IOException exception){
            throw new ShortLinksRemovalError(exception);
        }
    }
}
