package com.toutoleron.formapp.auth.controller;

import com.toutoleron.formapp.auth.AnnuaireProxy;
import com.toutoleron.formapp.auth.config.ApplicationPropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private ApplicationPropertiesConfiguration properties;
    @Autowired
    private AnnuaireProxy annuaireProxy;

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(properties.getProperty());
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> admin() {
        return ResponseEntity.ok("hello admin");
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> user() {
        return ResponseEntity.ok("hello user");
    }


    @GetMapping("/healthCheck")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok("ok");
    }

}
