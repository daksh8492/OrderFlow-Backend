 package com.orderflow.security;

import com.orderflow.entity.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

 @Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractCode(String token){
        return extractAllClaims(token).getSubject();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

     public boolean isTokenExpired(String token) {
         return extractClaim(token, claims -> claims.getExpiration()).before(new Date());
     }

     public boolean isTokenValid(String token, UserDetails userDetails) {
        String code = extractCode(token);
        return code.equals(userDetails.getUsername()) && !isTokenExpired(token);
     }

    public String generateAccessToken(CustomUserPrincipal principal) {
        User user = principal.getUser();
        return Jwts.builder()
                .subject(user.getCode())
                .claim("fieldOfWork",user.getFieldOfWork().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(CustomUserPrincipal principal) {
        User user = principal.getUser();
        return Jwts.builder()
                .subject(user.getCode())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+refreshTokenExpiration))
                .signWith(getSigningKey())
                .compact();
    }

}
