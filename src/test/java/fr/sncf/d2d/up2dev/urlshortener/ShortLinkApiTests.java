package fr.sncf.d2d.up2dev.urlshortener;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@ContextConfiguration(initializers = ShortenedLinksDatabaseTestInitializer.class)
@AutoConfigureMockMvc
class ShortLinkApiTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ShortLinksDatabaseTestUtils databaseTestUtils;

	@Nested
	class CreateTests {
		/**
		 * Créer un lien raccourci, sans erreur.
		 * @throws Exception
		 */
		@Test
		@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
		void create() throws Exception {
			mockMvc.perform(
				post("/links")
					.contentType(MediaType.APPLICATION_JSON)
					.content("\"http://www.google.com/\"")
			).andExpectAll(			
				status().isCreated(),
				jsonPath("$.id").isString(),
				jsonPath("$.real-url").value("http://www.google.com/"),
				jsonPath("$.short-id").isString(),
				header().exists("X-Removal-Token")
			);

			Assertions.assertEquals(4, databaseTestUtils.get().size(), "links database must contain 4 links");
		}

		/**
		 * Créer un lien raccourci avec une URL invalide.
		 * @throws Exception
		 */
		@Test
		void invalidUrls() throws Exception {
			for (final var invalid: List.of("http://localhost:8080", "ftp://somewhere.com")){
				mockMvc.perform(
					post("/links")
						.contentType(MediaType.APPLICATION_JSON)
						.content(String.format("\"%s\"", invalid))
				).andExpectAll(			
					status().isBadRequest(),
					jsonPath("$").value("invalid url")
				);
			}

			Assertions.assertEquals(3, databaseTestUtils.get().size(), "links database must not contain any new link");
		}
	}

	@Nested
	class DeleteTests {

		/**
		 * Supprimer un lien raccourci avec un token valide
		 */
		@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
		@Test
		void remove() throws Exception {
			mockMvc.perform(
				delete("/links/7132e9a9-e8f5-49c7-8472-e8167def87c7")
					.header("X-Removal-Token", "cd2b6a3ac1db9cfe26608373041fef14")
			).andExpect(status().isNoContent());

			Assertions.assertEquals(2, databaseTestUtils.get().size(), "links database must now contain 2 links");
		}

		/**
		 * Supprimer un lien raccourci avec un token invalide
		 */
		@Test
		void forbidden() throws Exception {
			mockMvc.perform(
				delete("/links/7132e9a9-e8f5-49c7-8472-e8167def87c7")
					.header("X-Removal-Token", "woops")
			).andExpect(status().isForbidden());
			Assertions.assertEquals(3, databaseTestUtils.get().size(), "links database must still contain 3 links");
		}

		/**
		 * Supprimer un lien raccourci avec un token invalide
		 */
		@Test
		void notFound() throws Exception {
			mockMvc.perform(
				delete("/links/eb21e398-2732-11ee-96dd-00155d521bb3")
					.header("X-Removal-Token", "ab901af96407a56d99952539450b3507")
			).andExpect(status().isNotFound());
			Assertions.assertEquals(3, databaseTestUtils.get().size(), "links database must still contain 3 links");
		}
	}

	@Nested
	class RedirectionTests {

		/** Accéder à un lien raccourci */
		@Test
		void redirect() throws Exception {
			mockMvc.perform(
				get("/5bC36ac4")
			).andExpectAll(
				status().is3xxRedirection(),
				header().string(HttpHeaders.LOCATION, "https://www.amazon.com/")
			);
		}
	}

}
