package com.example.apitoregisterphone;

import com.example.apitoregisterphone.common.Request.RegisterRequest;
import com.example.apitoregisterphone.services.Impl.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.example.apitoregisterphone.models.users.Role.ADMIN;
import static com.example.apitoregisterphone.models.users.Role.MANAGER;

@SpringBootApplication
public class ApiToRegisterPhoneApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiToRegisterPhoneApplication.class, args);
    }
    
    @Bean
    public CommandLineRunner commandLineRunner(AuthenticationService authenticationService) {
        return args -> {
            var admin = RegisterRequest.builder()
                    .firstName("Admin")
                    .lastName("Admin")
                    .username("admin")
                    .password("password")
                    .role(ADMIN)
                    .build();
            System.out.println("Admin token: " + authenticationService.register(admin).getAccessToken());

            var manager = RegisterRequest.builder()
                    .firstName("Admin")
                    .firstName("Admin")
                    .username("manager")
                    .password("password")
                    .role(MANAGER)
                    .build();
            System.out.println("Manager token: " + authenticationService.register(manager).getAccessToken());
        };
    }

}
