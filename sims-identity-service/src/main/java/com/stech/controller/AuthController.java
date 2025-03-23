package com.stech.controller;

import com.stech.dto.AuthRequest;
import com.stech.entity.UserCredential;
import com.stech.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public String addNewUser(@RequestBody UserCredential credential) {
        return authService.saveUser(credential);
    }

    @GetMapping("/token")
    public String getToken(@RequestBody AuthRequest authRequest) {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername()
            ,authRequest.getPassword()));
            if(authentication.isAuthenticated()){
                return authService.generateToken(authRequest.getUsername());
            } else {
                throw new RuntimeException("Invalid access!!");
            }
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        authService.validateToken(token);
        return "Token is valid";
    }
}
