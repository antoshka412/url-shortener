package com.antonina.urlshortener.service;

import com.antonina.urlshortener.util.Base62Encoder;
import org.springframework.stereotype.Service;

@Service
public class UrlShortenerService {

    public String shortenUrl() {
        return "http://short.ly/" + Base62Encoder.generateCode();
    }
}
