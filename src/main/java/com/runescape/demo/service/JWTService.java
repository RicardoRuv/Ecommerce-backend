package com.runescape.demo.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.runescape.demo.model.LocalUser;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiration}")
    private int expiration;

    private Algorithm algorithm;

    private static final String USERNAME_KEY = "username";

    @PostConstruct
    public void postConstruct() {
        algorithm = Algorithm.HMAC256(secret);
    }

    public String generateToken(LocalUser localUser) {
        return JWT.create()
                .withClaim(USERNAME_KEY, localUser.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + (expiration * 1000)))
                .withIssuer(issuer)
                .sign(algorithm);
    }


}
