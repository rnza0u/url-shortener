package fr.sncf.d2d.up2dev.urlshortener.shortlinks.persistence;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShortLinkPersistenceConfiguration {
    
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizeObjectMapper(ShortLinkDatabaseSerializer serializer, ShortLinkDatabaseDeserializer deserializer){
        return builder -> builder
            .serializers(serializer)
            .deserializers(deserializer)
            .build();
    }
}
