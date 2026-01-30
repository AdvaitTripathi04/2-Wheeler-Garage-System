package com.garage.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GarageSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(GarageSystemApplication.class, args);
		System.out.println("Spring Boot is Running...");
	}

	@org.springframework.context.annotation.Bean
	public org.springframework.boot.CommandLineRunner demo(com.garage.system.repository.UserRepository repository) {
		return (args) -> {
			if (repository.findByUsername("admin").isEmpty()) {
				repository.save(new com.garage.system.model.User("admin", "admin123", "ADMIN"));
				System.out.println("Admin User Created: admin / admin123");
			}
		};
	}

}
