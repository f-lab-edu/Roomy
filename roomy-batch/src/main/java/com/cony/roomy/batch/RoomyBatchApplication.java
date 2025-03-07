package com.cony.roomy.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients(basePackages = "com.cony.roomy.core")
@EntityScan(basePackages = "com.cony.roomy.core")
@EnableJpaRepositories(basePackages = "com.cony.roomy.core")
@SpringBootApplication(scanBasePackages = {"com.cony.roomy"})
@EnableScheduling
@EnableJpaAuditing
public class RoomyBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomyBatchApplication.class, args);
    }

}
