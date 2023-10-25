package fr.sncf.d2d.up2dev.urlshortener.shortlinks.persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import fr.sncf.d2d.up2dev.urlshortener.shortlinks.models.ShortLink;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class ShortLinksRepository {

    public static final String REPOSITORY_FILE_PATH_CONFIGURATION_KEY = "application.links-path";

    private final Path repositoryFilePath;

    private final ObjectMapper objectMapper;

    public ShortLinksRepository(
        @Value("${" + REPOSITORY_FILE_PATH_CONFIGURATION_KEY + "}") Path repositoryFilePath, 
        ObjectMapper objectMapper
    ){
        this.repositoryFilePath = repositoryFilePath;
        this.objectMapper = objectMapper;
    }

    /**
     * Liens stockés en mémoire, à ce stade la base de données se résume à ce simple dictionnaire.
     */
    private final Map<UUID, ShortLink> links = new HashMap<>();
    
    // Ces deux dictionnaires font office d'index pour accéder rapidement à un lien via différents types d'identifiants.
    // Sans ces deux index, on serait forcés de parcourir potentiellement l'ensemble du dic principal à chaque recherche
    // de lien.
    private final Map<UUID, UUID> primaryIdIndex = new HashMap<>();
    private final Map<String, UUID> shortIdIndex = new HashMap<>();

    /**
     * Initialise le repository avec les données existantes dans le fichier JSON persistant
     * Créé le fichier s'il n'existe pas.
     * @throws IOException en cas d'erreur d'écriture sur le disque.
     */
    public synchronized InitState init() throws IOException {
        if (!Files.exists(this.repositoryFilePath)){
            this.write();
            return InitState.NEW_FILE;
        }
        try (final var stream = new FileInputStream(this.repositoryFilePath.toFile())){
            for (final var link: this.objectMapper.readValue(stream, new TypeReference<List<ShortLink>>() {})){
                this.insert(link);
            }
        };
        return InitState.FILE_EXISTS;
    }

    /**
     * Chercher un lien raccourci via son identifiant unique.
     * @param id l'identifiant
     * @return un {@link Optional} contenant le lien si trouvé, vide autrement.
     */
    public Optional<ShortLink> findById(UUID id){
        return Optional.ofNullable(this.primaryIdIndex.get(id)).map(this.links::get);
    }

    /**
     * Chercher un lien raccourci via son identifiant court.
     * @param id l'identifiant court
     * @return un {@link Optional} contenant le lien si trouvé, vide autrement.
     */
    public Optional<ShortLink> findByShortId(String shortId){
        return Optional.ofNullable(this.shortIdIndex.get(shortId)).map(this.links::get);
    }

    /**
     * Enregistrer un lien raccourci en base de données.
     * @param link le lien à enregistrer
     * @throws IOException en cas d'erreur lors de l'écriture sur disque
     */
    public synchronized void save(ShortLink link) throws IOException {
        this.insert(link);
        this.write();
    }

    /**
     * Supprimer des liens raccourcis de la base de données en se basant sur un prédicat.
     * @param predicate le prédicat utilisé pour supprimer ou non un lien.
     * @throws IOException en cas d'erreur lors de l'écriture sur disque
     */
    public synchronized long deleteWhere(Predicate<ShortLink> predicate) throws IOException {
        final var toDelete = this.links.values().stream().filter(predicate).toList();
        for (final var link: toDelete)
            this.remove(link);
        this.write();
        return toDelete.size();
    }

    private void insert(ShortLink link){
        final var memoryId = UUID.randomUUID();
        this.links.put(memoryId, link);
        this.primaryIdIndex.put(link.getId(), memoryId);
        this.shortIdIndex.put(link.getShortId(), memoryId);
    }

    private void remove(ShortLink link){
        
        if (!this.primaryIdIndex.containsKey(link.getId()))
            throw new NoSuchElementException("link was not found in database");
        
        final var memoryId = this.primaryIdIndex.get(link.getId());
        
        this.links.remove(memoryId);
        this.primaryIdIndex.remove(link.getId());
        this.shortIdIndex.remove(link.getShortId());
    }

    private void write() throws IOException {
        this.objectMapper.writeValue(this.repositoryFilePath.toFile(), this.links.values());
    }

    public static enum InitState {
        NEW_FILE,
        FILE_EXISTS
    }
}
