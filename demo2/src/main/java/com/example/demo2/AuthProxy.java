package com.example.demo2;

import com.example.demo2.model.Role;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(name = "auth", url = "${auth.base-path}")
@Component
public interface AuthProxy {

    @GetMapping("/roles")
    public Set<Role> getUserRoles(@RequestHeader("Authorization") String authorization);

}
