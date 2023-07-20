package fr.sncf.d2d.up2dev.urlshortener.shortlinks.persistence;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

import fr.sncf.d2d.up2dev.urlshortener.shortlinks.models.ShortLink;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

@Component
public class ShortLinkDatabaseDeserializer extends StdDeserializer<ShortLink> {

    public ShortLinkDatabaseDeserializer(){
        this(ShortLink.class);
    }

    protected ShortLinkDatabaseDeserializer(Class<ShortLink> vc) {
        super(vc);
    }

    @Override
    public ShortLink deserialize(JsonParser parser, DeserializationContext context) throws IOException, JacksonException {

        final JsonNode root = parser.getCodec().readTree(parser);

        return ShortLink.builder()
            .id(UUID.fromString(root.get("id").textValue()))
            .realUrl(URI.create(root.get("realUrl").asText()).toURL())
            .shortId(root.get("shortId").asText())
            .removalToken(root.get("removalToken").asText())
            .createdAt(Date.from(Instant.parse(root.get("createdAt").asText())))
            .lastAccessedAt(Date.from(Instant.parse(root.get("lastAccessedAt").asText())))
            .build();
    }
    
}
