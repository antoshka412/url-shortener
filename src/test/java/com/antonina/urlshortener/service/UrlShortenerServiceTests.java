package com.antonina.urlshortener.service;

import com.antonina.urlshortener.cassandra.entity.UrlMapping;
import com.antonina.urlshortener.cassandra.repository.UrlMappingRepository;
import com.antonina.urlshortener.redis.CachingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlShortenerServiceTests {

    @Mock
    private UrlMappingRepository urlMappingRepository;

    @Mock
    private CachingService cachingService;

    @InjectMocks
    private UrlShortenerService urlShortenerService;

    @Test
    void generateShortCode_shouldReturnNonNullCode() {
        String code = urlShortenerService.generateShortCode();
        assertNotNull(code);
        assertFalse(code.isEmpty());
    }

    @Test
    void saveMapping_shouldCallRepositorySave() {
        String shortUrl = "abc123";
        String originalUrl = "https://example.com";

        urlShortenerService.saveMapping(shortUrl, originalUrl);

        verify(urlMappingRepository, times(1)).save(argThat(mapping -> mapping.getShortCode().equals(shortUrl) && mapping.getOriginalUrl().equals(originalUrl)));
    }

    @Test
    void getOriginalUrl_shouldReturnOriginalUrlIfExists() {
        String shortCode = "abc123";
        String originalUrl = "https://example.com";

        when(cachingService.getOriginalUrl(shortCode)).thenReturn(Optional.empty());
        when(urlMappingRepository.findById(shortCode)).thenReturn(Optional.of(new UrlMapping(shortCode, originalUrl)));

        String result = urlShortenerService.getOriginalUrl(shortCode);

        assertEquals(originalUrl, result);
    }

    @Test
    void getOriginalUrl_shouldReturnNullIfNotFound() {
        when(urlMappingRepository.findById("notfound")).thenReturn(Optional.empty());
        when(cachingService.getOriginalUrl("notfound")).thenReturn(Optional.empty());

        String result = urlShortenerService.getOriginalUrl("notfound");

        assertNull(result);
    }


    @Test
    void findById_shouldReturnTrueIfExists() {
        when(urlMappingRepository.findById("abc123")).thenReturn(Optional.of(new UrlMapping("abc123", "https://example.com")));

        assertTrue(urlShortenerService.existsByShortCode("abc123"));
    }

    @Test
    void findById_shouldReturnFalseIfNotExists() {
        when(urlMappingRepository.findById("xyz789")).thenReturn(Optional.empty());

        assertFalse(urlShortenerService.existsByShortCode("xyz789"));
    }
}


