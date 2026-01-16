package com.fooddelivery.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class RateLimitService {

    private final Cache<String, Bucket> cache;
    
    @Value("${rate.limit.public:20}")
    private int publicLimit;
    
    @Value("${rate.limit.authenticated:100}")
    private int authenticatedLimit;
    
    @Value("${rate.limit.payment:10}")
    private int paymentLimit;
    
    @Value("${rate.limit.window:60}")
    private int windowSeconds;

    public RateLimitService() {
        this.cache = Caffeine.newBuilder()
                .expireAfterAccess(2, TimeUnit.HOURS)
                .maximumSize(10_000)
                .build();
    }

    /**
     * Get or create a rate limit bucket for a given key
     * @param key Unique identifier (IP address or user ID)
     * @param limitType Type of limit (PUBLIC, AUTHENTICATED, PAYMENT)
     * @return Bucket instance
     */
    public Bucket resolveBucket(String key, LimitType limitType) {
        String cacheKey = key + ":" + limitType.name();
        
        return cache.get(cacheKey, k -> {
            int limit = getLimitForType(limitType);
            // Use Bandwidth.simple for non-deprecated API in Bucket4j 8.x
            Bandwidth bandwidth = Bandwidth.simple(limit, Duration.ofSeconds(windowSeconds));
            return Bucket.builder()
                    .addLimit(bandwidth)
                    .build();
        });
    }

    /**
     * Check if a request is allowed
     * @param key Unique identifier
     * @param limitType Type of limit
     * @return true if allowed, false if rate limit exceeded
     */
    public boolean tryConsume(String key, LimitType limitType) {
        Bucket bucket = resolveBucket(key, limitType);
        return bucket.tryConsume(1);
    }

    /**
     * Get remaining tokens
     * @param key Unique identifier
     * @param limitType Type of limit
     * @return Number of remaining tokens
     */
    public long getAvailableTokens(String key, LimitType limitType) {
        Bucket bucket = resolveBucket(key, limitType);
        return bucket.getAvailableTokens();
    }

    /**
     * Get the limit for a given type
     * @param limitType Type of limit
     * @return Limit value
     */
    private int getLimitForType(LimitType limitType) {
        return switch (limitType) {
            case PUBLIC -> publicLimit;
            case AUTHENTICATED -> authenticatedLimit;
            case PAYMENT -> paymentLimit;
        };
    }

    /**
     * Get the window size in seconds
     * @return Window size
     */
    public int getWindowSeconds() {
        return windowSeconds;
    }

    /**
     * Get the limit for a given type
     * @param limitType Type of limit
     * @return Limit value
     */
    public int getLimitForTypePublic(LimitType limitType) {
        return getLimitForType(limitType);
    }

    public enum LimitType {
        PUBLIC,
        AUTHENTICATED,
        PAYMENT
    }
}

