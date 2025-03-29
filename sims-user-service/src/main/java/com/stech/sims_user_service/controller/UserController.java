package com.stech.sims_user_service.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @GetMapping
     public String getUser(){
         return "sanjay user";
     }

    @PreAuthorize("hasAuthority('ADMIN')")
   @GetMapping("/admin")
    public String admin(){
        return "sanjay admin";
    }
}
