package com.example.linkshortener.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.example.linkshortener.model.ShortUrl;
import com.example.linkshortener.service.UrlService;


@RestController
@RequestMapping("/api")
// @CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = {
    "http://localhost:3000",
    "https://url-shortner-assessement.netlify.app"
})
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody Map<String, String> body) {
        String originalUrl = body.get("url");
        try {
            ShortUrl shortened = urlService.shortenUrl(originalUrl);
            return ResponseEntity.ok(Map.of("shortUrl", "https://url-shortener-bxjf.onrender.com/api/" + shortened.getId()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }
    @GetMapping("/{id}")
    public RedirectView redirectToOriginalUrl(@PathVariable String id) {
        Optional<ShortUrl> shortUrlOpt = urlService.findById(id);  // <-- use this

        if (shortUrlOpt.isPresent()) {
            ShortUrl shortUrl = shortUrlOpt.get();
            LocalDateTime expiryTime = shortUrl.getCreatedAt().plusMinutes(5);

            if (LocalDateTime.now().isAfter(expiryTime)) {
                // return new RedirectView("http://localhost:3000/error?reason=expired");
                return new RedirectView("https://url-shortner-assessement.netlify.app/error?reason=expired");
                
            }

            return new RedirectView(shortUrl.getOriginalUrl());
        }

        // return new RedirectView("http://localhost:3000/error?reason=not_found");
        return new RedirectView("https://url-shortner-assessement.netlify.app/error?reason=not_found");
    }
    @GetMapping("/urls")
    public ResponseEntity<?> getAllShortenedUrls() {
        return ResponseEntity.ok(urlService.getAllUrlsWithStatus());
    }


}