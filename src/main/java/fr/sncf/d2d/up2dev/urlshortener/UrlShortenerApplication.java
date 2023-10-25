package fr.sncf.d2d.up2dev.urlshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
/** Pour effectuer des tâches planifiées, on doit activer manuellement le module de scheduling de Spring. */
@EnableScheduling
public class UrlShortenerApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(UrlShortenerApplication.class, args);

		while (true){

			// taches planifiées

			Thread.sleep(100000);
		}
	}

}
