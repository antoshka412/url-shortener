package com.antonina.urlshortener.controller;

import com.antonina.urlshortener.model.UrlShortenerRequest;
import com.antonina.urlshortener.model.UrlShortenerResponse;
import com.antonina.urlshortener.service.UrlShortenerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/url")
public class  UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<UrlShortenerResponse> shortenUrl(@RequestBody UrlShortenerRequest request) {
        String shortUrl = urlShortenerService.shortenUrl();
        return ResponseEntity.ok(new UrlShortenerResponse(shortUrl));
    }
}
