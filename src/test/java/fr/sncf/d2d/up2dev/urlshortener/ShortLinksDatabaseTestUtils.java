package fr.sncf.d2d.up2dev.urlshortener;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ShortLinksDatabaseTestUtils {

    private final Path linksFilePath;
    private final ObjectMapper objectMapper;
    
    public ShortLinksDatabaseTestUtils(@Value("${application.links-path}") Path linksFilePath, ObjectMapper objectMapper){
        this.linksFilePath = linksFilePath;
        this.objectMapper = objectMapper;
    }

    public List<Map<String, String>> get() throws Exception {
        return this.objectMapper.readValue(
            this.linksFilePath.toFile(), 
            new TypeReference<List<Map<String, String>>>() {}
        );
    }
}
