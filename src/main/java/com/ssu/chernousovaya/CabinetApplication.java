package com.ssu.chernousovaya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EntityScan("com.ssu.chernousovaya")
@ComponentScan("com.ssu.chernousovaya")
public class CabinetApplication {

    public static void main(String[] args) {
        SpringApplication.run(CabinetApplication.class, args);
    }
}
