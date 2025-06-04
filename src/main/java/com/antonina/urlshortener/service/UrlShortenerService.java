package com.antonina.urlshortener.service;

import com.antonina.urlshortener.cassandra.entity.UrlMapping;
import com.antonina.urlshortener.cassandra.repository.UrlMappingRepository;
import com.antonina.urlshortener.redis.CachingService;
import com.antonina.urlshortener.util.Base62Encoder;
import org.springframework.stereotype.Service;

@Service
public class UrlShortenerService {
    private final UrlMappingRepository urlMappingRepository;
    private final CachingService cachingService;

    public UrlShortenerService(UrlMappingRepository urlMappingRepository, CachingService cachingService) {
        this.urlMappingRepository = urlMappingRepository;
        this.cachingService = cachingService;
    }

    public String generateShortCode() {
        return Base62Encoder.generateCode();
    }

    public void saveMapping(String shortUrl, String originalUrl) {
        UrlMapping mapping = new UrlMapping(shortUrl, originalUrl);
        urlMappingRepository.save(mapping);
    }

    public String getOriginalUrl(String shortUrl) {
        return this.cachingService.getOriginalUrl(shortUrl)
                .or(() -> urlMappingRepository
                        .findById(shortUrl)
                        .map(UrlMapping::getOriginalUrl)).orElse(null);
    }

    public boolean existsByShortCode(String shortCode) {
        return urlMappingRepository.findById(shortCode).isPresent();
    }

}
