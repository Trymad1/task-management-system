package com.trymad.task_management.security.jwt;

import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.trymad.task_management.model.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    private final Duration expiration;
    private final SecretKey secret;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") Duration expiration) {
        this.expiration = expiration;
        this.secret = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    public String generateToken(UserDetails userDetails) {
        final Map<String, Object> claims = new HashMap<>();
        final List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roles);

        final Date now = new Date();
        final Date expiredDate = new Date(now.getTime() + expiration.toMillis());
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(expiredDate)
                .signWith(secret)
                .compact();
    }

    public String refresh(String token) {
        final Claims claims = this.getClaims(token);
        final String subject = this.getMail(token);

        final Date now = new Date();
        final Date expiredDate = new Date(now.getTime() + expiration.toMillis());
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(expiredDate)
                .signWith(secret)
                .compact();
    }

    public String getMail(String token) {
        return this.getClaims(token).getSubject();
    }

    public Set<Role> getRoles(String token) {
        @SuppressWarnings("unchecked")
        List<String> roles = this.getClaims(token).get("roles", List.class);
        return roles.stream().map(Role::valueOf).collect(Collectors.toSet());
    }

    public boolean isExpired(String token) {
        return this.getExpirationDate(token).after(new Date());
    }

    public Date getExpirationDate(String token) {
        return this.getClaims(token).getExpiration();
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
