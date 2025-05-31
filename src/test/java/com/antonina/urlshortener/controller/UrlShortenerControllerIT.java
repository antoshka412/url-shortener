package com.antonina.urlshortener.controller;

import com.antonina.urlshortener.IntegrationTestBase;
import com.antonina.urlshortener.model.UrlShortenerRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UrlShortenerControllerIT extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldShortenAndRedirectUrl() throws Exception {
        UrlShortenerRequest request = new UrlShortenerRequest("https://example.com");
        String json = objectMapper.writeValueAsString(request);

        String response = mockMvc.perform(post("/api/url/shorten").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String shortUrl = objectMapper.readTree(response).get("shortUrl").asText();
        String shortCode = shortUrl.substring(shortUrl.lastIndexOf("/") + 1);

        mockMvc.perform(get("/api/url/" + shortCode)).andExpect(status().isFound()).andExpect(header().string("Location", "https://example.com"));
    }

}
