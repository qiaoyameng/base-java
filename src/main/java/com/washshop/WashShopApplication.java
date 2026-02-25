package com.washshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WashShopApplication {
    public static void main(String[] args) {
        SpringApplication.run(WashShopApplication.class, args);
    }
}
