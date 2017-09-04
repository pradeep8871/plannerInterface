package com.opta.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class OptaPlanningAppConfig {

    public static void main(String[] args) {
        SpringApplication.run(OptaPlanningAppConfig.class, args);
    }
}
