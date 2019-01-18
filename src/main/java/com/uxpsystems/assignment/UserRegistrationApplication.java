package com.uxpsystems.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
//@Import(JpaBaseConfiguration.class)
@SpringBootApplication(scanBasePackages={"com.uxpsystems.assignment"})
@EnableAsync
public class UserRegistrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserRegistrationApplication.class, args);
    }
}
