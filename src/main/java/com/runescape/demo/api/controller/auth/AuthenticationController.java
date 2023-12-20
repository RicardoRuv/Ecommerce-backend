package com.runescape.demo.api.controller.auth;

import com.runescape.demo.api.model.LoginBody;
import com.runescape.demo.api.model.LoginResponse;
import com.runescape.demo.api.model.RegistrationBody;
import com.runescape.demo.exception.UserAlreadyExistsException;
import com.runescape.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    public ResponseEntity registerUser(@Valid @RequestBody RegistrationBody registrationBody){
        try {
            userService.registerUser(registrationBody);
            return ResponseEntity.ok().build();
        }catch (UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginBody loginBody){
        String token = userService.login(loginBody);
        if(token != null){
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setJwt(token);
            return ResponseEntity.ok(loginResponse);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
