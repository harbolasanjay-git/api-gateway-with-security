package com.stech.sims_user_service.cnfig;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import java.util.List;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("[SecurityConfig] Security filter chain initialized! ");
        http
                .csrf(AbstractHttpConfigurer::disable)
                .securityContext(securityContext -> securityContext
                        .securityContextRepository(new HttpSessionSecurityContextRepository()) // Persist security context
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/error", "/error/**").permitAll()
                        .requestMatchers("/user").hasAnyAuthority("USER","ADMIN")
                        .requestMatchers("/admin").hasAuthority("ADMIN")
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex.accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied!");
                }))
                .anonymous(AbstractHttpConfigurer::disable)
                .addFilterBefore(new RoleHeaderFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
/*
    @Bean
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<?>> voters = List.of(new RoleVoter(), new AuthenticatedVoter());
        return new AffirmativeBased(voters);
    }*/

}
