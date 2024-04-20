package com.chenxi.finease;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.chenxi.finease")
@EntityScan({"com.chenxi.finease.model"})
@EnableJpaRepositories("com.chenxi.finease.repository")

public class FineaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(FineaseApplication.class, args);
	}

}