package com.chat.server.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

public class CustomCorsConfig {
    public CustomCorsConfig(CorsConfigurer<HttpSecurity> c) {
        CorsConfigurationSource source = request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(Collections.singletonList("*"));
            config.setAllowedHeaders(
                    Arrays.asList("Origin", "Content-Type", "Accept", "responseType", "Authorization"));
            config.setAllowedMethods(Arrays.asList("GET", "OPTIONS", "POST", "PUT", "DELETE", "HEAD", "PATCH"));
            return config;
        };
        c.configurationSource(source);
    }
}
