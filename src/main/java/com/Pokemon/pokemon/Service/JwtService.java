package com.Pokemon.pokemon.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRETO;

    @Value("${jwt.expiration}")
    private long expirationTime;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRETO.getBytes());
    }

    public Claims extraerContenidoClaims(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(getSigningKey())
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }

    public String extraerUsername(String token) {
        return extraerContenidoClaims(token).getSubject();
    }

    public Date extraerTiempoVencimiento(String token) {
        return extraerContenidoClaims(token).getExpiration();
    }

    public boolean isTokenExpiration(String token) {
        return extraerTiempoVencimiento(token).before(new Date());
    }

    public String prepararEstructuraToken(Map<String, Object> payload, String subject) {
        return Jwts.builder()
                   .setClaims(payload)
                   .setSubject(subject)
                   .setIssuedAt(new Date(System.currentTimeMillis()))
                   .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                   .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                   .compact();
    }

    public String creatToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return prepararEstructuraToken(claims, userDetails.getUsername());
    }

    public boolean validarToken(String token, UserDetails userDetails) {
        final String username = extraerUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpiration(token));
    }
}
