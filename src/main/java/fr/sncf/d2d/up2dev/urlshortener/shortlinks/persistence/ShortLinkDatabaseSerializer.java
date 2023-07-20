package fr.sncf.d2d.up2dev.urlshortener.shortlinks.persistence;

import java.io.IOException;

import org.springframework.stereotype.Component;

import fr.sncf.d2d.up2dev.urlshortener.shortlinks.models.ShortLink;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

@Component
public class ShortLinkDatabaseSerializer extends StdSerializer<ShortLink> {

    public ShortLinkDatabaseSerializer(){
        this(ShortLink.class);
    }

    protected ShortLinkDatabaseSerializer(Class<ShortLink> t) {
        super(t);
    }

    @Override
    public void serialize(ShortLink link, JsonGenerator serializer, SerializerProvider provider) throws IOException {
        serializer.writeStartObject();
        serializer.writeStringField("id", link.getId().toString());
        serializer.writeStringField("realUrl", link.getRealUrl().toString());
        serializer.writeStringField("shortId", link.getShortId());
        serializer.writeStringField("removalToken", link.getRemovalToken());
        serializer.writeStringField("createdAt", link.getCreatedAt().toInstant().toString());
        serializer.writeStringField("lastAccessedAt", link.getLastAccessedAt().toInstant().toString());
        serializer.writeEndObject();
    }

    
    
}
