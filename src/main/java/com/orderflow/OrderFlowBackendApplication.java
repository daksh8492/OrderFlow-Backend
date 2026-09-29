package com.orderflow;

import com.orderflow.entity.user.FieldOfWork;
import com.orderflow.entity.user.User;
import com.orderflow.repository.user.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class OrderFlowBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderFlowBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner createInitialAdmin(
			UserRepo userRepo,
			PasswordEncoder passwordEncoder
	) {
		return args -> {

			if (userRepo.count() == 0) {

				User user = User.builder()
						.name("Admin")
						.code("USR-0001")
						.fieldOfWork(FieldOfWork.ADMIN)
						.password(passwordEncoder.encode("USR-0001"))
						.userWarehouse(null)
						.build();

				userRepo.save(user);
			}
		};
	}

}
