package fr.sncf.d2d.up2dev.urlshortener.shortlinks.models;

import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public final class ShortLink {
    
    // nombre de jours avant l'expiration d'un lien si non accédé
    public static final Duration DURATION_BEFORE_EXPIRATION = Duration.ofDays(30);

    private final UUID id;
    
    // l'URL vers laquelle on doit rediriger lors de l'accès au lien raccourci, autrement dit l'URL réelle
    private final URL realUrl;

    // l'identifiant unique pour le lien raccourci
    private final String shortId;

    // token unique permettant la suppression du lien raccourci
    private final String removalToken;

    // date de création du lien raccourci
    private final Date createdAt;

    // date de dernier accès au lien raccourci
    private Date lastAccessedAt;

    private ShortLink(UUID id, URL realUrl, String shortId, String removalToken, Date createdAt, Date lastAccessedAt) {
        
        this.id = Objects.requireNonNull(id, "short link id cannot be null");
        this.realUrl = Objects.requireNonNull(realUrl, "short link URL cannot be null");
        this.shortId = Objects.requireNonNull(shortId, "short link short id cannot be null");
        this.removalToken = Objects.requireNonNull(removalToken, "short link removal token cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "short link creation date cannot be null");
        this.lastAccessedAt = Objects.requireNonNull(lastAccessedAt, "short link last access date cannot be null");
        
        if (this.lastAccessedAt.toInstant().isBefore(this.createdAt.toInstant()))
            throw new IllegalStateException("short link last access date must not be before creation date");
        
        if (this.removalToken.isBlank())
            throw new IllegalStateException("short link removal token cannot be blank");
        
        if (this.shortId.isBlank())
            throw new IllegalStateException("short link short id cannot be blank");
    }

    public UUID getId() {
        return this.id;
    }

    public boolean isExpired(Instant now){
        return this.lastAccessedAt.toInstant().plus(DURATION_BEFORE_EXPIRATION).isBefore(now);
    }

    public URL access(Instant now){
        this.lastAccessedAt = Date.from(now);
        return this.getRealUrl();
    }

    public URL getRealUrl() {
        return this.realUrl;
    }

    public String getShortId() {
        return this.shortId;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public String getRemovalToken(){
        return this.removalToken;
    }

    public Date getLastAccessedAt() {
        return this.lastAccessedAt;
    }

    public static ShortLinkBuilder builder(){
        return new ShortLinkBuilder();
    }

    /** Builder pour la classe {@link ShortLink} */
    public static class ShortLinkBuilder {

        private UUID id;
    
        private URL realUrl;

        private String shortId;

        private String removalToken;

        private Date createdAt;

        private Date lastAccessedAt;

        public ShortLinkBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public ShortLinkBuilder realUrl(URL realUrl) {
            this.realUrl = realUrl;
            return this;
        }

        public ShortLinkBuilder shortId(String shortId) {
            this.shortId = shortId;
            return this;
        }

        public ShortLinkBuilder removalToken(String removalToken) {
            this.removalToken = removalToken;
            return this;
        }

        public ShortLinkBuilder createdAt(Date createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ShortLinkBuilder lastAccessedAt(Date lastAccessedAt) {
            this.lastAccessedAt = lastAccessedAt;
            return this;
        }

        public ShortLink build(){
            return new ShortLink(
                this.id, 
                this.realUrl, 
                this.shortId, 
                this.removalToken, 
                this.createdAt, 
                this.lastAccessedAt
            );
        }
    }
}
