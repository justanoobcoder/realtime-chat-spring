package com.chat.server.security.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtProperties {
    @Value("${app.security.auth.jwt.secret-key}")
    private String secretKey;

    @Value("${app.security.auth.jwt.expiration-in-milliseconds}")
    private long expirationInMilliseconds;
}
