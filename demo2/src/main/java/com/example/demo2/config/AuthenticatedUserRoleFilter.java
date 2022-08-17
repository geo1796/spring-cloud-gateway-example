package com.example.demo2.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.demo2.AuthProxy;
import com.example.demo2.jwt.JwtUtil;
import com.example.demo2.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AuthenticatedUserRoleFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthProxy authProxy;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticatedUserRoleFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            String jwt = parseJwt(request);
            User user = new User(jwtUtil.getUserNameFromJwtToken(jwt), request.getHeader("Authorization"));
            PreAuthenticatedAuthenticationToken auth
                    = new PreAuthenticatedAuthenticationToken(
                    user, null,
                    authProxy.getUserRoles(request.getHeader("Authorization")).stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList());

            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        catch (JWTVerificationException e){
            logger.error("Cannot set user authentication: " + e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }
}
