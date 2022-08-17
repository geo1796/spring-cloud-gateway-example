package com.example.demo2;

import com.example.demo2.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicReference;

@SpringBootApplication
@RestController
@EnableDiscoveryClient
@EnableFeignClients("com.example.demo2")
public class Demo2Application implements CommandLineRunner {

	@Autowired
	public DemoProxy demoProxy;
	private static final Logger logger = LoggerFactory.getLogger(Demo2Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Demo2Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}

	@GetMapping("/demo2Service")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> demo2(){
		System.out.println("demo2 : calling method 'demo2'");
		return ResponseEntity.ok("demo2 microservice");
	}

	@GetMapping("/demoService")
	//@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> personFromDemoProxy() {
		logger.info("demo2 : calling method 'personFromDemoProxy'");
		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		logger.info("Principal {username = " + principal.getUsername() + " | token = " + principal.getToken() + "}");
		return ResponseEntity.ok(demoProxy.person(principal.getToken()));
	}

	@GetMapping("/healthCheck")
	public ResponseEntity<?> healthCheck() {
		return ResponseEntity.ok("ok");
	}
}
