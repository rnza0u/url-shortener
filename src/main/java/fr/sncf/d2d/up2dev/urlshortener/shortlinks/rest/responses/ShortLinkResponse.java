package fr.sncf.d2d.up2dev.urlshortener.shortlinks.rest.responses;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.sncf.d2d.up2dev.urlshortener.shortlinks.models.ShortLink;

public class ShortLinkResponse {
    
    private final UUID id;

    private final String shortId;

    private final String realUrl;

    public ShortLinkResponse(UUID id, String shortId, String realUrl) {
        this.id = id;
        this.shortId = shortId;
        this.realUrl = realUrl;
    }

    @JsonProperty("id")
    public UUID getId() {
        return this.id;
    }

    @JsonProperty("short-id")
    public String getShortId() {
        return this.shortId;
    }

    @JsonProperty("real-url")
    public String getRealUrl() {
        return this.realUrl;
    }

    public static ShortLinkResponse from(ShortLink link){
        return new ShortLinkResponse(
            link.getId(), 
            link.getShortId(), 

            link.getRealUrl().toString()
        );
    }
}
