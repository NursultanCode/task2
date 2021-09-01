package com.bank.testbank.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Component
public class JwtTokenUtil implements Serializable {
    public static final long JWT_TOKEN_VALIDITY_FOR_LOGIN = 24 * 60 * 60;

    @Value("${jwt.secret}")
    private String secret;

    public String getUserNameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }
    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String generateTokenForString(String subject){
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, subject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.after(new Date());
    }

    public String generateTokenForLogin(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return doGenerateForLogin(claims, userDetails.getUsername());
    }

    private String doGenerateForLogin(Map<String,Object> claims, String subject){
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY_FOR_LOGIN*1000))
                .signWith(SignatureAlgorithm.HS512,secret).compact();
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY_FOR_LOGIN * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = getUserNameFromToken(token);
        return (username.equals(userDetails.getUsername())&& isTokenExpired(token));
    }




}
