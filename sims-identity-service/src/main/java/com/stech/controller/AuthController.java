package com.stech.controller;

import com.stech.dto.AuthRequest;
import com.stech.dto.RoleDTO;
import com.stech.dto.UserDTO;
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
    public String addNewUser(@RequestBody UserDTO userDTO) {
        return authService.saveUser(userDTO);
    }

    @PostMapping("/load-roles")
    public String loadRoles(RoleDTO roleDTO) {
        return authService.loadRoles(roleDTO);
    }

    @GetMapping("/token")
    public String getToken(@RequestBody AuthRequest authRequest) {
        System.out.println("Auth Request : " + authRequest.getUsername() +  " " + authRequest.getPassword());
        System.out.println("Auth Request : " + authRequest.getUsername() +  " " + authRequest.getPassword());
        Authentication authentication =  null;
        try {
             authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername()
                    , authRequest.getPassword()));
        } catch (Exception e){
            System.out.println("Bad Credentials");
            return "Invalid username or password!";
        }
        System.out.println("Is User Authenticated : "+authentication.isAuthenticated());
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
