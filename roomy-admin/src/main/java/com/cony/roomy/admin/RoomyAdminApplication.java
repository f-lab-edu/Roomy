package com.cony.roomy.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@EntityScan(basePackages = "com.cony.roomy.core")
@EnableJpaRepositories(basePackages = "com.cony.roomy.core")
@SpringBootApplication(scanBasePackages = {"com.cony.roomy"})
public class RoomyAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomyAdminApplication.class, args);
    }

}
