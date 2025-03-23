package com.stech.service;

import com.stech.entity.UserCredential;
import com.stech.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserCredentialRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Transactional
    public String saveUser(UserCredential credential) {
        System.out.println("Auth Service execution started..............................................");
        System.out.println(credential.getId()+" "+credential.getName() +" " +credential.getEmail() +" "+credential.getPassword());
        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
        repository.save(credential);
        return  "User added to the system!!";
    }

    public String generateToken(String userName) {
        return jwtService.generateToken(userName);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
}
