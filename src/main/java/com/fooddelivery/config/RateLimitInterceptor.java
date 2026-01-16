package com.fooddelivery.config;

import com.fooddelivery.service.RateLimitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RateLimitService rateLimitService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String key = getKey(request);
        RateLimitService.LimitType limitType = getLimitType(request);

        if (!rateLimitService.tryConsume(key, limitType)) {
            // Rate limit exceeded
            long availableTokens = rateLimitService.getAvailableTokens(key, limitType);
            int limit = rateLimitService.getLimitForTypePublic(limitType);
            int windowSeconds = rateLimitService.getWindowSeconds();
            
            // Calculate reset time (current time + window seconds)
            long resetTime = Instant.now().getEpochSecond() + windowSeconds;
            
            // Set rate limit headers
            response.setHeader("X-RateLimit-Limit", String.valueOf(limit));
            response.setHeader("X-RateLimit-Remaining", String.valueOf(availableTokens));
            response.setHeader("X-RateLimit-Reset", String.valueOf(resetTime));
            response.setHeader("Retry-After", String.valueOf(windowSeconds));
            
            // Return 429 Too Many Requests
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write(String.format(
                "{\"status\":429,\"message\":\"Rate limit exceeded. Maximum %d requests per %d seconds.\",\"timestamp\":\"%s\",\"path\":\"%s\"}",
                limit, windowSeconds, Instant.now().toString(), request.getRequestURI()
            ));
            return false;
        }

        // Rate limit not exceeded, set headers
        long availableTokens = rateLimitService.getAvailableTokens(key, limitType);
        int limit = rateLimitService.getLimitForTypePublic(limitType);
        int windowSeconds = rateLimitService.getWindowSeconds();
        long resetTime = Instant.now().getEpochSecond() + windowSeconds;

        response.setHeader("X-RateLimit-Limit", String.valueOf(limit));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(availableTokens));
        response.setHeader("X-RateLimit-Reset", String.valueOf(resetTime));

        return true;
    }

    /**
     * Get unique key for rate limiting (IP address or user ID)
     */
    private String getKey(HttpServletRequest request) {
        // Try to get authenticated user from SecurityContext
        String username = null;
        try {
            org.springframework.security.core.Authentication authentication = 
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && 
                !(authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
                username = authentication.getName();
            }
        } catch (Exception e) {
            // Ignore if security context is not available
        }

        if (username != null) {
            return "user:" + username;
        }

        // Fall back to IP address
        String ipAddress = getClientIpAddress(request);
        return "ip:" + ipAddress;
    }

    /**
     * Get client IP address from request
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

    /**
     * Determine rate limit type based on request path
     */
    private RateLimitService.LimitType getLimitType(HttpServletRequest request) {
        String path = request.getRequestURI();

        // Payment endpoints
        if (path.startsWith("/api/payments/")) {
            return RateLimitService.LimitType.PAYMENT;
        }

        // Authenticated endpoints (require authentication)
        if (path.startsWith("/api/customer/") ||
            path.startsWith("/api/cart/") ||
            path.startsWith("/api/orders/") ||
            path.startsWith("/api/restaurant/") ||
            path.startsWith("/api/delivery/") ||
            path.startsWith("/api/admin/") ||
            path.startsWith("/api/reviews/submit") ||
            path.startsWith("/api/reviews/my-reviews")) {
            return RateLimitService.LimitType.AUTHENTICATED;
        }

        // Public endpoints (no authentication required)
        return RateLimitService.LimitType.PUBLIC;
    }
}

