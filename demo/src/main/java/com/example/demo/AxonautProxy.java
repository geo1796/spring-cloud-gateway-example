package com.example.demo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(name = "axonaut", url = "https://axonaut.com/api/v2")
@Component
public interface AxonautProxy {

    @GetMapping("/employees")
    public Set<?> employees(@RequestHeader("userApiKey") String key, @RequestHeader("page") int page);

}
