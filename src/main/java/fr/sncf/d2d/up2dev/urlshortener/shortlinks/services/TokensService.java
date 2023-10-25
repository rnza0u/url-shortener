package fr.sncf.d2d.up2dev.urlshortener.shortlinks.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import fr.sncf.d2d.up2dev.urlshortener.shortlinks.exceptions.ShortLinkIdentifierGenerationError;

/**
 * Service métier exposant des méthodes de création/manipulation d'identifiants.
 */
@Service
public class TokensService {

    private final String[] SHORT_ID_ALPHABET = 
        IntStream.concat(
            IntStream.range('a', 'z' + 1), 
            IntStream.concat(
                IntStream.range('A', 'Z' + 1),
                IntStream.range('0', '9' + 1)
            )
        ).mapToObj(i -> String.valueOf((char)i)).toArray(String[]::new);

    /**
     * Générer un token de suppression de lien raccourci.
     * @return Un token aléatoire de suppression de lien raccourci
     * @throws ShortLinkIdentifierGenerationError en cas d'erreur de génération
     */
    public String generateRemovalToken() throws ShortLinkIdentifierGenerationError {
        try {
            final var bytes = new byte[32];
            SecureRandom.getInstanceStrong().nextBytes(bytes);
            return HexFormat.of().formatHex(bytes);
        } catch(NoSuchAlgorithmException ex){
            throw new ShortLinkIdentifierGenerationError(ex);
        }
    }

    /**
     * Vérifie la validité d'un token de suppression de lien raccourci.
     * @param actual le token attendu
     * @param provided le token à valider
     * @return {@code} true si le token est valide.
     */
    public boolean isRemovalTokenValid(String actual, String provided){
        // cette méthode est à privilégier pour effectuer des opérations de comparaison sur une valeur secrète.
        // Une simple comparaison de chaînes avec .equals() ne se fait pas en temps constant.
        // voir https://codahale.com/a-lesson-in-timing-attacks/
        return MessageDigest.isEqual(actual.getBytes(), provided.getBytes());
    }

    /**
     * Générer un identifiant court pour les liens raccourcis.
     * @return Un identifiant court aléatoire.
     * @throws ShortLinkIdentifierGenerationError en cas d'erreur de génération
     */
    public String generateShortId() throws ShortLinkIdentifierGenerationError {
        try {
            final var bytes = new byte[8];
            SecureRandom.getInstanceStrong().nextBytes(bytes);
            return IntStream.range(0, bytes.length)
                .mapToObj(i -> bytes[i])
                .map(i -> SHORT_ID_ALPHABET[Byte.toUnsignedInt(i) % SHORT_ID_ALPHABET.length])
                .collect(Collectors.joining());
        } catch(NoSuchAlgorithmException ex){
            throw new ShortLinkIdentifierGenerationError(ex);
        }
    }
}
