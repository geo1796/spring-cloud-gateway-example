package com.example.gateway.filter;

import com.example.gateway.jwt.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtGatewayFilterFactory.Config> {

    private static final Logger logger = LoggerFactory.getLogger(JwtGatewayFilterFactory.class);
    @Autowired
    private JwtUtil jwtUtil;

    public JwtGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(JwtGatewayFilterFactory.Config config) {

        return ((exchange, chain) -> {
            HttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey("Authorization")) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            final String token = parseJwt(request.getHeaders().getOrEmpty("Authorization").get(0));

            if (!jwtUtil.validateJwtToken(token)) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            return chain.filter(exchange.mutate().request(ServerHttpRequest.Builder::build).build());
        });

    }

    private String parseJwt(String token) {
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7, token.length());
        }
        return null;
    }

    public static class Config {
        @Value("${security.jwtSecret")
        private String jwtSecret;

        @Value("${security.jwtExpiration}")
        private int jwtExpiration;
    }
}
