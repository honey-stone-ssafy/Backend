package com.honeystone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HoneyStoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(HoneyStoneApplication.class, args);
	}

}
