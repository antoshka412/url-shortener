package com.antonina.urlshortener.model;

public class UrlShortenerResponse {
    private String shortenedUrl;

    public UrlShortenerResponse(String shortenedUrl) {
        this.shortenedUrl = shortenedUrl;
    }

    public String getShortenedUrl() {
        return shortenedUrl;
    }

    public void setShortenedUrl(String shortenedUrl) {
        this.shortenedUrl = shortenedUrl;
    }
}

