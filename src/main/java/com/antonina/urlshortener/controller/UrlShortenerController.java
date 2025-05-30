package com.antonina.urlshortener.controller;

import com.antonina.urlshortener.model.ErrorResponse;
import com.antonina.urlshortener.model.UrlShortenerRequest;
import com.antonina.urlshortener.model.UrlShortenerResponse;
import com.antonina.urlshortener.service.UrlShortenerService;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/url")
public class UrlShortenerController {
    private static final UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});
    private final UrlShortenerService urlShortenerService;
    @Value("${app.short-domain}")
    private String shortDomain;

    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody UrlShortenerRequest request) {
        String originalUrl = request.url();

        if (!urlValidator.isValid(originalUrl)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Invalid URL format."));
        }

        // Generate a unique short code
        String code;
        do {
            code = urlShortenerService.generateShortCode();
        } while (urlShortenerService.existsByShortCode(code));

        // Save the new mapping
        urlShortenerService.saveMapping(code, originalUrl);

        return ResponseEntity.ok(new UrlShortenerResponse(shortDomain + code, code));
    }


    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortCode) {
        String originalUrl = urlShortenerService.getOriginalUrl(shortCode);

        if (originalUrl != null) {
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalUrl)).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
