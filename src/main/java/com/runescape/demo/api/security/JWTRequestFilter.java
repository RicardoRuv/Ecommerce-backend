package com.runescape.demo.api.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.runescape.demo.api.model.dao.LocalUserDAO;
import com.runescape.demo.model.LocalUser;
import com.runescape.demo.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private JWTService jwtService;
    private LocalUserDAO localUserDAO;

    public JWTRequestFilter(JWTService jwtService, LocalUserDAO localUserDAO) {
        this.jwtService = jwtService;
        this.localUserDAO = localUserDAO;
    }

    /**
     * Every request will go through this filter. First check if it has a header named "Authorization".
     * If it does, check if it starts with "Bearer ". If it does, get the token from the header.
     * Then decode the token and get the username from it. Then check if the user exists.
     * If the user exists, set the authentication.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Get the token from the header.
        String tokenHeader = request.getHeader("Authorization");
        // If the token is not null and starts with "Bearer ", get the username from the token.
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            // Remove the "Bearer " part and get the username from the token.
            String token = tokenHeader.substring(7);
            try {
                // Find the user by the username.
                String username = jwtService.getUsernameFromToken(token);
                // Check if the user exists.
                Optional <LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(username);
                // If the user exists, set the authentication.
                if(opUser.isPresent()){
                    LocalUser user = opUser.get();
                    // Create the authentication object.
                    UsernamePasswordAuthenticationToken  authentication = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                    //set the details of the authentication so that Spring knows that the user is authenticated.
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // where the authentication is set.
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JWTDecodeException e) {
            }
        }
        filterChain.doFilter(request, response);
    }
}
