package com.pickcar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan(basePackages = {
        "com.pickcar.domain",
})
@SpringBootApplication
public class EmulatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmulatorApplication.class, args);
    }
}