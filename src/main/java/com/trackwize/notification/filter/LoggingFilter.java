package com.trackwize.notification.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

/**
 * LoggingFilter is a custom filter that logs HTTP requests and responses.
 * It captures request and response details, including headers, body, and processing time.
 * It also generates a unique correlation ID for each request.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingFilter extends OncePerRequestFilter {

    private static final String TRACKING_ID = "X-Tracking-ID";
    private static final String USER_ID = "X-User-ID";
    private static final String REQUEST_TIME = "X-Request-Time";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String trackingId = request.getHeader(TRACKING_ID);
        if (trackingId == null || trackingId.isBlank()) {
            trackingId = UUID.randomUUID().toString();
            log.debug("Generated tracking ID: {}", trackingId);
        }

        String userId = request.getHeader(USER_ID);
        Instant requestTime = Instant.now();

        MDC.put("trackingId", trackingId);
        if (userId != null) {
            MDC.put("userId", userId);
        }

        response.setHeader(TRACKING_ID, trackingId);
        response.setHeader(REQUEST_TIME, requestTime.toString());
        if (userId != null) {
            response.setHeader(USER_ID, userId);
        }

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            log.info("\n");
            log.info("Incoming request: method={}, URI={}, userId={}",
                    wrappedRequest.getMethod(),
                    wrappedRequest.getRequestURI(),
                    userId != null ? userId : "anonymous");

            filterChain.doFilter(wrappedRequest, wrappedResponse);

        } finally {
            wrappedResponse.copyBodyToResponse();
            MDC.clear();
        }
    }
}

