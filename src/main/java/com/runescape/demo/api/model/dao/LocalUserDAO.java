package com.runescape.demo.api.model.dao;

import com.runescape.demo.model.LocalUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LocalUserDAO extends CrudRepository<LocalUser, Long> {
    boolean findByUsername(String username);
    boolean findByEmail(String email);

    Optional<LocalUser> findByUsernameIgnoreCase(String username);

    Optional<LocalUser> findByEmailIgnoreCase(String email);



}
