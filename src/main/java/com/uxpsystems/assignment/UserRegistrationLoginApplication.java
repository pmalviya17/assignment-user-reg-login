package com.uxpsystems.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class UserRegistrationLoginApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserRegistrationLoginApplication.class, args);
    }
}
