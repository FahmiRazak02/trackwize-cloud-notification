package com.trackwize.cloud.notification.filter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class LoggingAdvice implements ResponseBodyAdvice<Object> {

    private static final int MAX_BODY_LOG_LENGTH = 500;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        if (!(response instanceof ServletServerHttpResponse httpResponse)
                || !(request instanceof ServletServerHttpRequest httpRequest)) {
            return body;
        }

        HttpServletResponse servletResponse = httpResponse.getServletResponse();

        try {
            // --- Calculate processing time ---
            String requestTimeStr = (String) httpRequest.getServletRequest().getAttribute("X-Request-Time");
            Instant requestTime = requestTimeStr != null ? Instant.parse(requestTimeStr) : Instant.now();
            Instant responseTime = Instant.now();
            long durationMs = Duration.between(requestTime, responseTime).toMillis();

            servletResponse.setHeader("X-Response-Time", responseTime.toString());
            servletResponse.setHeader("X-Processing-Time", durationMs + "ms");

            // --- Prepare log context ---
            String trackingId = servletResponse.getHeader("X-Tracking-ID");
            String userId = servletResponse.getHeader("X-User-ID");

            // --- Log response details ---
            log.info("[{}] Outgoing response: status={}, userId={}, time={}ms, headers={}, body={}",
                    trackingId != null ? trackingId : "-",
                    servletResponse.getStatus(),
                    userId != null ? userId : "anonymous",
                    durationMs,
                    formatHeaders(servletResponse),
                    formatBody(body)
            );

        } catch (Exception e) {
            log.warn("Failed to log response: {}", e.getMessage());
        }

        return body;
    }

    private String formatHeaders(HttpServletResponse response) {
        return response.getHeaderNames().stream()
                .map(header -> header + "=" + response.getHeader(header))
                .collect(Collectors.joining(", ", "{", "}"));
    }

    private String formatBody(Object body) {
        if (body == null) return "<empty>";

        String content = (body instanceof Map || body instanceof Iterable)
                ? body.toString()
                : String.valueOf(body);

        if (content.length() > MAX_BODY_LOG_LENGTH) {
            return content.substring(0, MAX_BODY_LOG_LENGTH) + "... (truncated)";
        }
        return content;
    }

}
