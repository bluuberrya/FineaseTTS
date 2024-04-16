package com.chenxi.finease;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.chenxi.finease"})
@EntityScan("com.chenxi.finease.model")
public class FineaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(FineaseApplication.class, args);
	}

}