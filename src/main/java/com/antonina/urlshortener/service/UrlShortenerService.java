package com.antonina.urlshortener.service;

import com.antonina.urlshortener.cassandra.entity.UrlMapping;
import com.antonina.urlshortener.cassandra.repository.UrlMappingRepository;
import com.antonina.urlshortener.util.Base62Encoder;
import org.springframework.stereotype.Service;

@Service
public class UrlShortenerService {
    private final UrlMappingRepository urlMappingRepository;

    public UrlShortenerService(UrlMappingRepository urlMappingRepository) {
        this.urlMappingRepository = urlMappingRepository;
    }

    public String shortenUrl(String originalUrl) {
        String shortenedUrl = "http://short.ly/" + Base62Encoder.generateCode();
        saveMapping(shortenedUrl, originalUrl);
        return shortenedUrl;
    }

    public void saveMapping(String shortUrl, String originalUrl) {
        UrlMapping mapping = new UrlMapping(shortUrl, originalUrl);
        urlMappingRepository.save(mapping);
    }

}
