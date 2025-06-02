package com.antonina.urlshortener.service;

import com.antonina.urlshortener.cassandra.entity.UrlMapping;
import com.antonina.urlshortener.cassandra.repository.UrlMappingRepository;
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

        verify(urlMappingRepository,
                times(1))
                .save(argThat(mapping -> mapping.getShortCode().equals(shortUrl)
                        && mapping.getOriginalUrl().equals(originalUrl)));
    }

    @Test
    void getOriginalUrl_shouldReturnOriginalUrlIfExists() {
        String shortUrl = "abc123";
        String originalUrl = "https://example.com";

        when(urlMappingRepository.findById(shortUrl)).thenReturn(Optional.of(new UrlMapping(shortUrl, originalUrl)));

        String result = urlShortenerService.getOriginalUrl(shortUrl);

        assertEquals(originalUrl, result);
    }

    @Test
    void getOriginalUrl_shouldReturnNullIfNotFound() {
        when(urlMappingRepository.findById("notfound")).thenReturn(Optional.empty());

        String result = urlShortenerService.getOriginalUrl("notfound");

        assertNull(result);
    }

    @Test
    void findById_shouldReturnTrueIfExists() {
        when(urlMappingRepository.findById("abc123"))
            .thenReturn(Optional.of(new UrlMapping("abc123", "https://example.com")));

        assertTrue(urlShortenerService.existsByShortCode("abc123"));
    }

    @Test
    void findById_shouldReturnFalseIfNotExists() {
        when(urlMappingRepository.findById("xyz789"))
            .thenReturn(Optional.empty());

        assertFalse(urlShortenerService.existsByShortCode("xyz789"));
    }
}


