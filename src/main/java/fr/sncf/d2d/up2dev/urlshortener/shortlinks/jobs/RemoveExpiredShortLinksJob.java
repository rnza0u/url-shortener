package fr.sncf.d2d.up2dev.urlshortener.shortlinks.jobs;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import fr.sncf.d2d.up2dev.urlshortener.shortlinks.exceptions.ShortLinksRemovalError;
import fr.sncf.d2d.up2dev.urlshortener.shortlinks.usecases.RemoveExpiredShortLinksUseCase;

/** Bean servant à mettre en place la tâche planifiée de suppresion des liens raccourcis expirés.  */
@Component
public class RemoveExpiredShortLinksJob {

    private final Logger logger = LoggerFactory.getLogger(RemoveExpiredShortLinksJob.class);
    
    private final RemoveExpiredShortLinksUseCase removeExpiredShortLinksUseCase;

    public RemoveExpiredShortLinksJob(RemoveExpiredShortLinksUseCase removeExpiredShortLinksUseCase){
        this.removeExpiredShortLinksUseCase = removeExpiredShortLinksUseCase;
    }

    // le job consiste à appeler la usecase toutes les 10 minutes, et logger le résultat en cas d'erreur.
    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
    public void start(){
        try {
            this.logger.info("{} expired links were deleted", this.removeExpiredShortLinksUseCase.remove());
        } catch (ShortLinksRemovalError e) {
            this.logger.error("an error occured while deleting expired short links", e);
        }
    }
}
