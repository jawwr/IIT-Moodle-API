package com.example.markservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MarkServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarkServiceApplication.class, args);
    }

}
