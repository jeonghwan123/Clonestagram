package com.goorm.clonestagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ClonestagramApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClonestagramApplication.class, args);
    }

}
