package com.goorm.clonestagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaAuditing
@EntityScan(basePackages = "com.goorm.clonestagram") // ✅ Entity 패키지 스캔 설정
@EnableJpaRepositories(basePackages = "com.goorm.clonestagram") // ✅ Repository 패키지 스캔 설정
@SpringBootApplication
public class ClonestagramApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClonestagramApplication.class, args);
    }

}
