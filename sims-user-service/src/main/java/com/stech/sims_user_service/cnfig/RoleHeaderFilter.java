package com.stech.sims_user_service.cnfig;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RoleHeaderFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String rolesHeader = request.getHeader("X-User-Roles");

        if (rolesHeader != null) {
            List<SimpleGrantedAuthority> authorities = Arrays.stream(rolesHeader.split(","))
                    .map(rol->rol.substring(rol.indexOf("_")+1))
                    .map(SimpleGrantedAuthority::new) // No ROLE_ prefix
                    .collect(Collectors.toList());

            User user = new User("authenticatedUser", "", authorities);
            PreAuthenticatedAuthenticationToken auth = new PreAuthenticatedAuthenticationToken(user, null, authorities);
            SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_THREADLOCAL);
            SecurityContextHolder.getContext().setAuthentication(auth);

        }

        filterChain.doFilter(request, response);
    }

}
