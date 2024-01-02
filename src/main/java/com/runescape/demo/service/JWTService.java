package com.runescape.demo.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.runescape.demo.model.LocalUser;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
/**
 * Json web token service.
 * Encrypted string that is used to authenticate a user. (Credential verification)
 * Holds the encryption algorithm, secret, issuer, and expiration on the front end.
 */
@Service
public class JWTService {

    // The secret is used to encrypt the JWT.
    @Value("${jwt.secret}")
    private String secret;
    // The issuer is the person who issued the JWT.
    @Value("${jwt.issuer}")
    private String issuer;
    // The expiration is the time until the JWT expires.
    @Value("${jwt.expiration}")
    private int expiration;
    // The algorithm is the encryption algorithm used to encrypt the JWT.
    private Algorithm algorithm;
    // The username key is the key used to get the username from the JWT.
    private static final String USERNAME_KEY = "username";

    /**
     * Post construct method that initializes the algorithm.
     */
    @PostConstruct
    public void postConstruct() {
        algorithm = Algorithm.HMAC256(secret);
    }

    /**
     * Generates a JWT token.
     * @param localUser The user.
     * @return The JWT token.
     */
    public String generateToken(LocalUser localUser) {
        return JWT.create()
                .withClaim(USERNAME_KEY, localUser.getUsername()) // Key to object mapping. This is how we get the username from the JWT.
                .withExpiresAt(new Date(System.currentTimeMillis() + (expiration * 1000))) // The expiration is set.
                .withIssuer(issuer) // Authority that issued the JWT.
                .sign(algorithm); // Ensures the integrity of the JWT.
    }

    public String getUsernameFromToken(String token) {
        return JWT.decode(token).getClaim(USERNAME_KEY).asString();
    }


}
