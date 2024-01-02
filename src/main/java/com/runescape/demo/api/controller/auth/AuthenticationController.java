package com.runescape.demo.api.controller.auth;

import com.runescape.demo.api.model.LoginBody;
import com.runescape.demo.api.model.LoginResponse;
import com.runescape.demo.api.model.RegistrationBody;
import com.runescape.demo.exception.UserAlreadyExistsException;
import com.runescape.demo.model.LocalUser;
import com.runescape.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private UserService userService;

    /**
     * Constructor for the controller.
     * @param userService The user service.
     */
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a user.
     * @param registrationBody  -- The information of the user.
     * @return http status code.
     */
    @PostMapping("/register")
    public ResponseEntity registerUser(@Valid @RequestBody RegistrationBody registrationBody){
        try {
            userService.registerUser(registrationBody);
            return ResponseEntity.ok().build();
        }catch (UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Logs in a user.
     * @param loginBody The information of the user.
     * @return The JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginBody loginBody){
        // If the user exists and the password is correct, return the JWT token.
        String token = userService.login(loginBody);
        if(token != null){
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setJwt(token);
            // Send the token back to the client as a json object.
            return ResponseEntity.ok(loginResponse);
        }
        // Otherwise, return unauthorized.
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /**
     * Gets the logged in user.
     * @param localUser The logged in user.
     * @return The logged in user.
     *
     * @AuthenticationPrincipal is used to access the currently authenticated user in the controllers.
     */
    @GetMapping("/me")
    public ResponseEntity<LocalUser> getLoggedInUser(@AuthenticationPrincipal LocalUser localUser){
        return ResponseEntity.ok(localUser);
    }
}
