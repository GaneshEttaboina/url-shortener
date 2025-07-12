package com.example.linkshortener.service;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.linkshortener.model.ShortUrl;
import com.example.linkshortener.repository.UrlRepository;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    private String normalizeUrl(String url) {
    	System.out.println("url = " + url);
        try {
            URI uri = new URI(url.trim());
            
            String scheme = uri.getScheme() != null ? uri.getScheme().toLowerCase() : "https";
            String host = uri.getHost() != null ? uri.getHost().toLowerCase().replaceFirst("^www\\.", "") : "";
            String path = uri.getPath() != null ? uri.getPath().replaceAll("/$", "") : "";
            String query = uri.getQuery() != null ? "?" + uri.getQuery() : "";
            System.out.println("Normalized URL: " + scheme + "://" + host + path + query);
            return scheme + "://" + host + path + query;
        } catch (Exception e) {
            return url.trim(); // fallback
        }
    }

    public ShortUrl shortenUrl(String originalUrl) {
        if (!isValidUrl(originalUrl)) {
            throw new IllegalArgumentException("Invalid URL format.");
        }
        System.out.println("Original URL: " + originalUrl);
        String normalizedUrl = normalizeUrl(originalUrl); //normalize here

        Optional<ShortUrl> existing = urlRepository.findByOriginalUrl(normalizedUrl);

        LocalDateTime now = LocalDateTime.now();

        if (existing.isPresent()) {
            ShortUrl existingUrl = existing.get();
            if (now.isBefore(existingUrl.getCreatedAt().plusMinutes(5))) {
                return existingUrl;
            } else {
                existingUrl.setCreatedAt(now);
                return urlRepository.save(existingUrl);
            }
        }

        // Create new short URL
        ShortUrl url = new ShortUrl();
        url.setId(UUID.randomUUID().toString().substring(0, 7));
        url.setOriginalUrl(normalizedUrl); // store normalized URL
        url.setCreatedAt(now);
        return urlRepository.save(url);
    }



    public Optional<ShortUrl> getOriginalUrl(String id) {
        Optional<ShortUrl> url = urlRepository.findById(id);
        if (url.isPresent()) {
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(url.get().getCreatedAt().plusMinutes(5))) {
                return url;
            }
        }
        return Optional.empty();
    }
    private boolean isValidUrl(String url) {
        String regex = "^(https?://)([\\w.-]+)(:[0-9]+)?(/.*)?$";
        return url.matches(regex);
    }


    public Optional<ShortUrl> findById(String id) {
        return urlRepository.findById(id); // Don't check for expiry here!
    }
    
    public List<Map<String, String>> getAllUrlsWithStatus() {
        LocalDateTime now = LocalDateTime.now();

        return urlRepository.findAll().stream()
            .map(url -> {
                boolean isExpired = now.isAfter(url.getCreatedAt().plusMinutes(5));
                return Map.of(
                    "originalUrl", url.getOriginalUrl(),
                    "shortUrl", "https://url-shortener-bxjf.onrender.com/api/" + url.getId(),
                    "status", isExpired ? "expired" : "active"
                );
            })
            .collect(Collectors.toList());
    }

    
}
