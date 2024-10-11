package com.huce.edu_v2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@SpringBootApplication
public class EduV2Application {

    public static void main(String[] args) {
        SpringApplication.run(EduV2Application.class, args);
    }

}
