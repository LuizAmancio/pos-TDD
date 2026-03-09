package com.pos.tdd.projectPayment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ProjectPaymentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectPaymentApplication.class, args);
    }
}
