package com.watchmode.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class WatchmodeApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WatchmodeApiApplication.class, args);
    }
}
