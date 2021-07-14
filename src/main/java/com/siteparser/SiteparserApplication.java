package com.siteparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SiteparserApplication {

	public static void main(String[] args) {
		SpringApplication.run(SiteparserApplication.class, args);
	}
}
