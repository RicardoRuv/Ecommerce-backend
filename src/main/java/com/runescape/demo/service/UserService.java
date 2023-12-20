package com.runescape.demo.service;

import com.runescape.demo.api.model.LoginBody;
import com.runescape.demo.api.model.RegistrationBody;
import com.runescape.demo.api.model.dao.LocalUserDAO;
import com.runescape.demo.exception.UserAlreadyExistsException;
import com.runescape.demo.model.LocalUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private LocalUserDAO localUserDAO;
    private EncryptionService encryptionService;
    private JWTService jwtService;

    /**
     * Constructor for the service. Injects the DAO and the encryption service.
     * @param localUserDAO The local user DAO.
     * @param encryptionService The encryption service.
     * @param jwtService The JWT service.
     */
    public UserService(LocalUserDAO localUserDAO, EncryptionService encryptionService, JWTService jwtService) {
        this.localUserDAO = localUserDAO;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
    }

    /**
     * Registers a user.
     * @param registrationBody The information of the user.
     * @return The user.
     * @throws UserAlreadyExistsException If the user already exists.
     */
    public LocalUser registerUser( RegistrationBody registrationBody) throws UserAlreadyExistsException {

        if(localUserDAO.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent() ||
                localUserDAO.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()){
            throw new UserAlreadyExistsException();
        }

        LocalUser localUser = new LocalUser();
        localUser.setUsername(registrationBody.getUsername());
        localUser.setEmail(registrationBody.getEmail());
        localUser.setFirstName(registrationBody.getFirstName());
        localUser.setLastName(registrationBody.getLastName());
        localUser.setPassword(encryptionService.encrypt(registrationBody.getPassword()));
        return localUserDAO.save(localUser);

    }
    /**
     * Logs in a user.
     * @param loginBody The information of the user.
     * @return The JWT token.
     */
    public String login(LoginBody loginBody){
        Optional<LocalUser> localUserOptional = localUserDAO.findByUsernameIgnoreCase(loginBody.getUsername());
        // If the user exists and the password is correct, return the JWT token.
        if(localUserOptional.isPresent()){
            LocalUser localUser = localUserOptional.get();
            //                            plain text password,    encrypted password
            if(encryptionService.check(loginBody.getPassword(), localUser.getPassword())){
                return jwtService.generateToken(localUser);
            }
        }
        return null;
    }
}
