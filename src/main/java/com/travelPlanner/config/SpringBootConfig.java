package com.travelPlanner.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;

/**
 * This Spring Configuration class, is used for starting the application using Spring Boot
 */
//@formatter:off
@SpringBootApplication(scanBasePackages = { "com.travelPlanner.rest.controller",
											"com.travelPlanner.persist", 
											"com.travelPlanner.service", 
											"com.travelPlanner.security" })
@EntityScan(basePackages = "com.travelPlanner.persist.entity")
//@formatter:on
public class SpringBootConfig extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(new Class[] { SpringBootConfig.class, WebConfig.class, SecurityConfig.class,
				WebSecurityConfiguration.class }, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SpringBootConfig.class);
	}
}
