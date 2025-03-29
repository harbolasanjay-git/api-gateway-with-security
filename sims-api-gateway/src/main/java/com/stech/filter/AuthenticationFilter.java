package com.stech.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import com.stech.util.JwtUtil;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(AuthenticationFilter.Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if(routeValidator.isSecure.test(exchange.getRequest())) {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                        throw new RuntimeException("missing Authorization Header");
                }
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {
//                    //REST call to AUTH service
//                    template.getForObject("http://IDENTITY-SERVICE//validate?token" + authHeader, String.class);
                    jwtUtil.validateToken(authHeader);
                    List<String> roles = jwtUtil.extractRoles(authHeader);
                    request = exchange.getRequest().mutate()
                            .header("X-User-Roles", String.join(",", roles))  // Pass roles to downstream services
                            .build();
                } catch (Exception e) {
                    System.out.println("Invalid access detected!");
                    return handleUnauthorizedResponse(exchange, "Unauthorized access to application!!", HttpStatus.UNAUTHORIZED);
                }
            }
            return chain.filter(exchange.mutate().request(request).build());
        });
    }

    public static class Config {

    }

    private Mono<Void> handleUnauthorizedResponse(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json");

        String errorJson = String.format("{\"error\": \"%s\", \"status\": %d}", message, status.value());
        byte[] bytes = errorJson.getBytes(StandardCharsets.UTF_8);

        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }

}
