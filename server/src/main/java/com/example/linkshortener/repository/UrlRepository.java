package com.example.linkshortener.repository;

import com.example.linkshortener.model.ShortUrl;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UrlRepository extends MongoRepository<ShortUrl, String> {
    Optional<ShortUrl> findByOriginalUrl(String originalUrl);
}