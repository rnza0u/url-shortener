package fr.sncf.d2d.up2dev.urlshortener.shortlinks.rest;

import java.net.URL;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.sncf.d2d.up2dev.urlshortener.shortlinks.rest.responses.ShortLinkResponse;
import fr.sncf.d2d.up2dev.urlshortener.shortlinks.usecases.CreateShortLinkUseCase;
import fr.sncf.d2d.up2dev.urlshortener.shortlinks.usecases.DeleteShortLinkUseCase;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/links")
public class ShortLinksApiRestController {

    private static final String REMOVE_TOKEN_HEADER_NAME = "X-Removal-Token";
    
    private final CreateShortLinkUseCase createShortLinkUseCase;
    private final DeleteShortLinkUseCase deleteShortLinkUseCase;

    public ShortLinksApiRestController(
        CreateShortLinkUseCase createShortLinkUseCase,
        DeleteShortLinkUseCase deleteShortLinkUseCase
    ){
        this.createShortLinkUseCase = createShortLinkUseCase;
        this.deleteShortLinkUseCase = deleteShortLinkUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShortLinkResponse create(@RequestBody URL url, HttpServletResponse response) throws Exception {
        final var link = this.createShortLinkUseCase.create(url);
        response.setHeader(REMOVE_TOKEN_HEADER_NAME, link.getRemovalToken());
        return ShortLinkResponse.from(link);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
        @PathVariable UUID id, 
        @RequestHeader(REMOVE_TOKEN_HEADER_NAME) String removalToken
    ) throws Exception {
        this.deleteShortLinkUseCase.delete(id, removalToken);
    }
}
