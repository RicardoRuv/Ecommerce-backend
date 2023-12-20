package com.runescape.demo.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {
    /**
     * The number of rounds to use when hashing the password.
     */
    @Value("${encryption.salt.rounds}")
    private int saltRounds;
    private String salt;

    /**
     * Initialize the salt.
     */
    @PostConstruct //This annotation is used on a method that needs to be executed after dependency injection is done to perform any initialization.
    public void init() {
        salt = BCrypt.gensalt(saltRounds);
    }

    /**
     * Encrypt the password.
     * @param password The password to encrypt.
     * @return The encrypted password.
     */
    public String encrypt(String password) {
        return BCrypt.hashpw(password, salt); //method used to hash a password using the BCrypt strong hashing function.
    }

    /**
     * Check if the password matches the hash.
     * @param password The password to check.
     * @param hash The hash to check against.
     * @return True if the password matches the hash, false otherwise.
     */
    public boolean check(String password, String hash) {
        return BCrypt.checkpw(password, hash); //method used to verify whether a plaintext password matches a previously hashed one.
    }
}
