package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Spring Boot application
 */
@SpringBootApplication
public class Application {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    /**
     * Start the Application
     * @param args the {@link String} array of arguments that can override default properties
     */
    public static void main(final String[] args) {
        LOG.info("Starting Application");
        SpringApplication.run(Application.class, args);
    }
}
