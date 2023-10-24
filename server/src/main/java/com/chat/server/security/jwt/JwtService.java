package com.chat.server.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class JwtService {
    @Value("${app.security.auth.jwt.secret-key}")
    private String secretKey;

    @Value("${app.security.auth.jwt.expiration-in-milliseconds}")
    private long expirationInMilliseconds;

    private final JwtBuilder jwtBuilder;
    private final JwtParser jwtParser;

    public JwtService() {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        jwtBuilder = Jwts.builder()
                .signWith(key);
        jwtParser = Jwts.parser()
                .verifyWith(key).build();
    }

    public String createToken(Authentication authentication) {
        String username = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findAny().orElse("ROLE_ANONYMOUS");
        Date expiration = new Date(System.currentTimeMillis() + expirationInMilliseconds);
        return jwtBuilder
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(expiration)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtParser.parseSignedClaims(token).getPayload();
        String username = claims.getSubject();
        String role = claims.get("role", String.class);
        var authorities = List.of(new SimpleGrantedAuthority(role));
        return new UsernamePasswordAuthenticationToken(username, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            jwtParser.parse(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}