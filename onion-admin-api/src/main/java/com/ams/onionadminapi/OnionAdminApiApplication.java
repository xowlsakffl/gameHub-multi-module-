package com.ams.onionadminapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "com.ams.onionadminapi",   // 현재 모듈
        "com.ams.onioncore",      // 서비스, 시큐리티, 예외처리
        "com.ams.oniondomain"     // 엔티티, 리포지토리
})
@EnableJpaRepositories(basePackages = "com.ams.oniondomain.repository")
@EntityScan(basePackages = "com.ams.oniondomain.entity")
public class OnionAdminApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnionAdminApiApplication.class, args);
    }

}
