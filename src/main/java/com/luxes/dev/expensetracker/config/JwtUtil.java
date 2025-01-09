package com.luxes.dev.expensetracker.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC256;

@Component
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String secretKey;
    @Value("${jwt.time.expiration}")
    private long expirationInMs;
    @Value("${jwt.issuer}")
    private String issuer;


    public String createToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuer(issuer)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationInMs))
                .sign(HMAC256(secretKey));
    }


    public boolean isValid(String jwt) {
        try {
            JWT.require(HMAC256(secretKey))
                    .build()
                    .verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String getUsername(String jwt) {
        return JWT.require(HMAC256(secretKey))
                .build()
                .verify(jwt)
                .getSubject();
    }


}
