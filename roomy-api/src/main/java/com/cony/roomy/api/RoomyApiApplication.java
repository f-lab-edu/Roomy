package com.cony.roomy.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients(basePackages = "com.cony.roomy.core")
@EnableJpaAuditing
@EntityScan(basePackages = "com.cony.roomy.core")
@EnableJpaRepositories(basePackages = "com.cony.roomy.core")
@SpringBootApplication(scanBasePackages = {"com.cony.roomy"})
public class RoomyApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoomyApiApplication.class, args);
	}

}
