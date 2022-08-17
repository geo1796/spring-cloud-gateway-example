package com.example.demo2.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${security.jwtSecret")
    private String jwtSecret;

    @Value("${security.jwtExpiration}")
    private int jwtExpiration;

    public String generateTokenFromUsername(String username, List<String> roles) {
        return JWT.create()
                .withSubject(username)
                .withClaim("roles", roles)
                .withIssuedAt(Date.from(Instant.now()))
                .withExpiresAt(Date.from(Instant.now().plus(this.jwtExpiration, ChronoUnit.MILLIS)))
                .sign(Algorithm.HMAC256(this.jwtSecret));
    }

    public String getUserNameFromJwtToken(String jwt) {
        if (jwt == null){
            throw new JWTVerificationException("token is null");
        }
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(this.jwtSecret)).build();
        DecodedJWT decodedJWT = verifier.verify(jwt);
        return decodedJWT.getSubject();
    }

    public List<String> getRolesFromJwtToken(String jwt) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(this.jwtSecret)).build();
        DecodedJWT decodedJWT = verifier.verify(jwt);
        return decodedJWT.getClaim("roles").asList(String.class);
    }

    public boolean validateJwtToken(String jwt) {
        if (jwt == null){
            return false;
        }

        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(this.jwtSecret)).build();
            verifier.verify(jwt);
        }
        catch (JWTVerificationException e){
            logger.error(e.getMessage());
            return false;
        }

        return true;
    }
}
