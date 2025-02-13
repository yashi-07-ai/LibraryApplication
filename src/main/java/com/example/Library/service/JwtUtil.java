package com.example.Library.service;

import com.example.Library.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;

import static javax.crypto.Cipher.SECRET_KEY;

@Component
public class JwtUtil {

    private static String secretKey = "GDbQJvfUdb2bahy/Q6pv2oJGS1bMXmM3ONL6OaP+GtfZ2exGve98KE/BWXEPKS4z";
    private static final long EXPIRATION_TIME = 60*60*1000; // 15 mins
//
//    public JwtUtil() throws NoSuchAlgorithmException {
//        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
//        //SecretKey sk = keyGen.generateKey();
//        secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
//    }
    public String generateToken(UserDetails userDetails) {
        User user = (User) userDetails;
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private static SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
    }

    public String extractUsername(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token has expired", e);
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Invalid JWT token", e);
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException("Unsupported JWT token", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("JWT token compact of handler are invalid", e);
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role", String.class);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    private Date getExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }
}

