package com.runescape.demo.service;

import com.runescape.demo.api.model.RegistrationBody;
import com.runescape.demo.api.model.dao.LocalUserDAO;
import com.runescape.demo.exception.UserAlreadyExistsException;
import com.runescape.demo.model.LocalUser;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private LocalUserDAO localUserDAO;

    public UserService(LocalUserDAO localUserDAO) {
        this.localUserDAO = localUserDAO;
    }

    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException {

        if(localUserDAO.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent() ||
                localUserDAO.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()){
            throw new UserAlreadyExistsException();
        }

        LocalUser localUser = new LocalUser();
        localUser.setUsername(registrationBody.getUsername());
        localUser.setEmail(registrationBody.getEmail());
        localUser.setFirstName(registrationBody.getFirstName());
        localUser.setLastName(registrationBody.getLastName());
        //TODO: Hash password
        localUser.setPassword(registrationBody.getPassword());
        return localUserDAO.save(localUser);

    }
}
