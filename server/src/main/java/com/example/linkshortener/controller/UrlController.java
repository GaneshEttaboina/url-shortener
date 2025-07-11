package com.example.linkshortener.controller;

import com.example.linkshortener.model.ShortUrl;
import com.example.linkshortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDateTime;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody Map<String, String> body) {
        String originalUrl = body.get("url");
        try {
            ShortUrl shortened = urlService.shortenUrl(originalUrl);
            return ResponseEntity.ok(Map.of("shortUrl", "http://localhost:7070/api/" + shortened.getId()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

//    @GetMapping("/{id}")
//    public RedirectView redirectToOriginalUrl(@PathVariable String id) {
//        Optional<ShortUrl> shortUrlOpt = urlService.getOriginalUrl(id);
//
//        if (shortUrlOpt.isPresent()) {
//            ShortUrl shortUrl = shortUrlOpt.get();
//            LocalDateTime expiryTime = shortUrl.getCreatedAt().plusMinutes(5);
//
//            if (LocalDateTime.now().isAfter(expiryTime)) {
//                // ✅ Link exists, but expired
//                return new RedirectView("http://localhost:3000/error?reason=expired");
//            }
//
//            // ✅ Valid and not expired — redirect to original URL
//            return new RedirectView(shortUrl.getOriginalUrl());
//        }
//
//        // ❌ Link not found
//        return new RedirectView("http://localhost:3000/error?reason=not_found");
//    }
    @GetMapping("/{id}")
    public RedirectView redirectToOriginalUrl(@PathVariable String id) {
        Optional<ShortUrl> shortUrlOpt = urlService.findById(id);  // <-- use this

        if (shortUrlOpt.isPresent()) {
            ShortUrl shortUrl = shortUrlOpt.get();
            LocalDateTime expiryTime = shortUrl.getCreatedAt().plusMinutes(5);

            if (LocalDateTime.now().isAfter(expiryTime)) {
                return new RedirectView("http://localhost:3000/error?reason=expired");
            }

            return new RedirectView(shortUrl.getOriginalUrl());
        }

        return new RedirectView("http://localhost:3000/error?reason=not_found");
    }
    @GetMapping("/urls")
    public ResponseEntity<?> getAllShortenedUrls() {
        return ResponseEntity.ok(urlService.getAllUrlsWithStatus());
    }


}