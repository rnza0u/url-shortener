package fr.sncf.d2d.up2dev.urlshortener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class ShortenedLinksDatabaseTestInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Path FIXTURE_PATH = Path.of("fixtures/test.links.json");

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        final var path = Path.of(applicationContext.getEnvironment().getProperty("application.links-path"));
        try {
            Files.deleteIfExists(path);
            Files.copy(FIXTURE_PATH, path);
        } catch (IOException e) {
            this.logger.error("error while refreshing test database", e);
            return;
        }
    }
    
}
