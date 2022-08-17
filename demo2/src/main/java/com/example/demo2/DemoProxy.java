package com.example.demo2;

import com.example.demo2.model.Person;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "demo", url = "${demo.base-path}")
@Component
public interface DemoProxy {

    @GetMapping("/person")
    Person person(@RequestHeader("Authorization") String username);

}
