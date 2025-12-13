package com.usermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // 🔥 TEST 1: To confirm this application is actually running
    @Bean
    public Runnable appStarted() {
        return () -> System.out.println("🔥🔥 SPRING BOOT APPLICATION STARTED SUCCESSFULLY 🔥🔥");
    }
}
