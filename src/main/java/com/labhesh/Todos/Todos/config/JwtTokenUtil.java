//package com.labhesh.Todos.Todos.config;
//
//
//import com.labhesh.Todos.Todos.app.user.Users;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.MalformedJwtException;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import io.jsonwebtoken.security.SignatureException;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
//@Component
//public class JwtTokenUtil {
//
//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.expiration}")
//    private long expiration;
//
//    public String generateToken(Users userDetails) {
//        return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
//                .setId(userDetails.getId().toString())
//                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256).compact();
//    }
//
//    public boolean validateToken(String token, UserDetails userDetails) throws SignatureException {
//        // Validate JWT token logic
//        return userDetails.getUsername().equals(extractUsername(token)) && !isTokenExpired(token);
//    }
//
//
//    private boolean isTokenExpired(String token) throws SignatureException, MalformedJwtException {
//        // Check if token is expired logic
//        return extractAllClaims(token).getExpiration().before(new Date());
//    }
//
//    public String extractUsername(String token) throws SignatureException, MalformedJwtException {
//        return extractAllClaims(token).getSubject();
//    }
//
//    private Claims extractAllClaims(String token) throws SignatureException, MalformedJwtException {
//        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secret.getBytes())).build().parseClaimsJws(token).getBody();
//    }
//
//
//}
