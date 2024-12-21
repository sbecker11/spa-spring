package com.spexture.spa_spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpaSpringApplication {

	private static final Logger logger = LoggerFactory.getLogger(SpaSpringApplication.class);

	public static void main(String[] args) {
		logger.info("Initializing SpaSpringApplication bean");
		SpringApplication.run(SpaSpringApplication.class, args);
	}
}
