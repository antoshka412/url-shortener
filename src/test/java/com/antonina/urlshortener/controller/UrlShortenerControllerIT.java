package com.antonina.urlshortener.controller;

import com.antonina.urlshortener.IntegrationTestBase;
import com.antonina.urlshortener.model.UrlShortenerRequest;
import com.antonina.urlshortener.model.UrlShortenerResponse;
import com.antonina.urlshortener.service.UrlShortenerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UrlShortenerControllerIT extends IntegrationTestBase {

    @Value("${app.short-domain}")
    private String shortDomain;

    @LocalServerPort
    private int port;
    @Autowired
    private UrlShortenerService urlShortenerService;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/urls";
    }

    @Test
    void testShortenEmptyUrl() {
        UrlShortenerRequest request = new UrlShortenerRequest("");
        ResponseEntity<String> response = restTemplate.postForEntity(getBaseUrl(), request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Invalid URL format");
    }

    @Test
    void testShortenNullUrl() {
        UrlShortenerRequest request = new UrlShortenerRequest(null);
        ResponseEntity<String> response = restTemplate.postForEntity(getBaseUrl(), request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Invalid URL format");
    }

    @Test
    void shouldShortenAndRedirectUrl() {
        String originalUrl = "https://example.com/";
        UrlShortenerRequest request = new UrlShortenerRequest(originalUrl);
        ResponseEntity<UrlShortenerResponse> response = restTemplate.postForEntity(getBaseUrl(), request, UrlShortenerResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        String shortCode = response.getBody().code();
        assertThat(shortCode).isNotBlank();

        assertThat(response.getBody().shortenedUrl()).isEqualTo(shortDomain + "/" + shortCode);

        // Check Cassandra persistence
        assertThat(urlShortenerService.existsByShortCode(shortCode)).isTrue();
        assertThat(urlShortenerService.getOriginalUrl(shortCode)).isEqualTo(originalUrl);

        ResponseEntity<Void> redirectResponse = restTemplate.getForEntity(getBaseUrl() + "/" + shortCode, Void.class);

        assertThat(redirectResponse.getHeaders().getLocation().toString()).isEqualTo(originalUrl);
        assertThat(redirectResponse.getStatusCode()).isEqualTo(HttpStatus.FOUND);
    }

    @Test
    void testShortenInvalidUrl() {
        UrlShortenerRequest request = new UrlShortenerRequest("ftp://invalid-url");
        ResponseEntity<String> response = restTemplate.postForEntity(getBaseUrl(), request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Invalid URL format");
    }

    @Test
    void testRedirectWithUnknownShortCode() {
        ResponseEntity<Void> response = restTemplate.getForEntity(getBaseUrl() + "/nonexistent123", Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
