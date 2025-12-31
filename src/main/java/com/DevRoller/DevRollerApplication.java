package com.devroller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DevRollerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevRollerApplication.class, args);
    }
}
