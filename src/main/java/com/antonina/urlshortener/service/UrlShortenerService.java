package com.antonina.urlshortener.service;

import com.antonina.urlshortener.cassandra.entity.UrlMapping;
import com.antonina.urlshortener.cassandra.repository.UrlMappingRepository;
import com.antonina.urlshortener.util.Base62Encoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UrlShortenerService {
    private final UrlMappingRepository urlMappingRepository;

    public UrlShortenerService(UrlMappingRepository urlMappingRepository) {
        this.urlMappingRepository = urlMappingRepository;
    }

    public String generateShortCode() {
        return Base62Encoder.generateCode();
    }

    public void saveMapping(String shortUrl, String originalUrl) {
        UrlMapping mapping = new UrlMapping(shortUrl, originalUrl);
        urlMappingRepository.save(mapping);
    }

    public String getOriginalUrl(String shortUrl) {
        Optional<UrlMapping> result = urlMappingRepository.findById(shortUrl);
        return result.map(UrlMapping::getOriginalUrl).orElse(null);
    }

    public boolean existsByShortCode(String shortCode) {
        return urlMappingRepository.existsByShortCode(shortCode);
    }

}
