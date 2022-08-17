package com.example.demo;

import com.example.demo.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@SpringBootApplication
@RestController
@EnableDiscoveryClient
@EnableFeignClients("com.example.demo")
public class DemoApplication {

	@Autowired
	private AxonautProxy axonautProxy;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping("/demoService")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> demo() {
		System.out.println("demo : calling method 'demo'");
		return ResponseEntity.ok("demo microservice");
	}

	@GetMapping("/person")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Person> person() {
		System.out.println("demo : calling method 'person'");
		return ResponseEntity.ok(new Person("Goku", "Son", 50));
	}

	@GetMapping("/healthCheck")
	public ResponseEntity<?> healthCheck() {
		return ResponseEntity.ok("ok");
	}
}
