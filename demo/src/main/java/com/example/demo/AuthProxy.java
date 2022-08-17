package com.example.demo;

import com.example.demo.model.Role;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Set;

@FeignClient(name = "auth", url = "${auth.base-path}")
@Component
public interface AuthProxy {

    @GetMapping("/roles")
    public Set<Role> getUserRoles(@RequestHeader("Authorization") String authorization);

}
