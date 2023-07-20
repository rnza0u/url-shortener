package fr.sncf.d2d.up2dev.urlshortener.shortlinks.web;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import fr.sncf.d2d.up2dev.urlshortener.shortlinks.usecases.AccessShortLinkUseCase;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ShortLinkRedirectionsController {

    private final AccessShortLinkUseCase accessShortLinkUseCase;

    public ShortLinkRedirectionsController(AccessShortLinkUseCase accessShortLinkUseCase){
        this.accessShortLinkUseCase = accessShortLinkUseCase;
    }
    
    @GetMapping("/{shortId}")
    public void accessShortLink(@PathVariable("shortId") String shortId, HttpServletResponse response) throws Exception {
        final var url = this.accessShortLinkUseCase.access(shortId);
        response.sendRedirect(url.toString());
    }
}
