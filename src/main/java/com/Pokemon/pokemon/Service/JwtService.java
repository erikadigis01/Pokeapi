package com.Pokemon.pokemon.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    
    @Value("${jwt.secret}")
    private String SECRETO;
    @Value("${jwt.expiration}")
    private long expirationTime;
    
    
     public Claims extraerContenidoClaims(String token){
        // parser: convierte a String, establece la clave para determinar si el JWT es valido dentro del header
        return Jwts.parser().setSigningKey(SECRETO).parseClaimsJws(token).getBody();
    } 
    
     public String extraerUsername(String token){
        //return extraerPartesToken(token,Claims::getSubject);
        return extraerContenidoClaims(token).getSubject();
    }
     
    public Date extraerTiempoVencimiento(String token){
        //return extraerPartesToken(token, Claims::getExpiration);
        return extraerContenidoClaims(token).getExpiration();
    }
    
    public boolean isTokenExpiration(String token){

        return extraerTiempoVencimiento(token).before(new Date());
    }
    
    public String prepararEstructuraToken(Map<String, Object> payload, String subject){
        return Jwts.builder()
                .setClaims(payload)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, SECRETO)
                .compact();
    }
    
    public String creatToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return prepararEstructuraToken(claims, userDetails.getUsername());
    }
    
    public boolean validarToken(String token, UserDetails userDetails){
        final String username = extraerUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpiration(token));
    }
}
