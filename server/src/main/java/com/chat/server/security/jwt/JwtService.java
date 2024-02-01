package com.chat.server.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtService {
    private final JwtProperties jwtProperties;
    private final JwtBuilder jwtBuilder;
    private final JwtParser jwtParser;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        // Create the JWT builder and parser using the secret key
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecretKey()));
        jwtBuilder = Jwts.builder()
                .signWith(key);
        jwtParser = Jwts.parser()
                .verifyWith(key).build();
    }

    public String createToken(Authentication authentication) {
        // Get the username and role from the authentication object
        String username = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findAny().orElse("ROLE_ANONYMOUS");
        // Create then return the token
        Date expiration = new Date(System.currentTimeMillis() + jwtProperties.getExpirationInMilliseconds());
        return jwtBuilder
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(expiration)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        // Get the username and role from the token
        Claims claims = jwtParser.parseSignedClaims(token).getPayload();
        String username = claims.getSubject();
        String role = claims.get("role", String.class);
        // Create then return the authentication object with the username, token, and
        // role
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
