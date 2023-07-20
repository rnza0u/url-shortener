package fr.sncf.d2d.up2dev.urlshortener.shortlinks.persistence;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    private final ShortLinksRepository repository;

    public DatabaseInitializer(ShortLinksRepository repository){
        this.repository = repository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            this.logger.info("Short links database was initialized ({})", this.repository.init());
        } catch (IOException exception){
            this.logger.error("Failed to initialize short links repository", exception);
        }
    }
    
}
