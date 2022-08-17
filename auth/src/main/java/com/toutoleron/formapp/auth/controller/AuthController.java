package com.toutoleron.formapp.auth.controller;

import com.toutoleron.formapp.auth.dao.RoleDao;
import com.toutoleron.formapp.auth.dao.UserDao;
import com.toutoleron.formapp.auth.exception.TokenRefreshException;
import com.toutoleron.formapp.auth.jwt.JwtUtil;
import com.toutoleron.formapp.auth.model.RefreshToken;
import com.toutoleron.formapp.auth.model.Role;
import com.toutoleron.formapp.auth.model.User;
import com.toutoleron.formapp.auth.payload.request.LoginRequest;
import com.toutoleron.formapp.auth.payload.request.RefreshTokenRequest;
import com.toutoleron.formapp.auth.payload.request.SignUpRequest;
import com.toutoleron.formapp.auth.payload.response.JwtResponse;
import com.toutoleron.formapp.auth.payload.response.RefreshTokenResponse;
import com.toutoleron.formapp.auth.service.RefreshTokenService;
import com.toutoleron.formapp.auth.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@AllArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private UserDao userDao;
    private RoleDao roleDao;
    private PasswordEncoder passwordEncoder;
    private DaoAuthenticationProvider daoAuthenticationProvider;
    private JwtUtil jwtUtil;
    private UserDetailsServiceImpl userDetailsService;
    private RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest){

        if (userDao.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(null);
        }

        Set<Role> roles = new HashSet<>();
        signUpRequest.getRoles().forEach(
                roleName -> {
                    Optional<Role> optionalRole = roleDao.findByName(roleName);
                    if (optionalRole.isEmpty()){
                        throw new IllegalArgumentException();
                    }
                    roles.add(optionalRole.get());
                }
        );

        userDao.save(new User(signUpRequest.getEmail(), passwordEncoder.encode(signUpRequest.getPassword()), roles));
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = daoAuthenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateTokenFromUsername(authentication.getName());
        List<String> roles = authentication.getAuthorities().stream().map(
                GrantedAuthority::getAuthority
        ).toList();

        return ResponseEntity.ok().body(
                new JwtResponse(
                        jwt,
                        refreshTokenService.createRefreshToken(authentication.getName()).getToken(),
                        authentication.getName(),
                        roles
                )
        );
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<Role> roles = authentication.getAuthorities()
                .stream().map(
                        grantedAuthority -> new Role(grantedAuthority.getAuthority())
                ).toList();

        return ResponseEntity.ok(roles);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtil.generateTokenFromUsername(user.getEmail());
                    return ResponseEntity.ok(new RefreshTokenResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }
}
