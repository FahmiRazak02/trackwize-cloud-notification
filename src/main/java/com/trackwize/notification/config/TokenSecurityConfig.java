package com.trackwize.notification.config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenSecurityConfig {

    private boolean enableTokenCookie = true;

    @Getter
    private boolean isHttps = false;

    public boolean isTokenCookieEnable() {
        return enableTokenCookie;
    }

}
