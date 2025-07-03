package com.pickcar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EntityScan(basePackages = {
        "com.pickcar.global.domain",
        "com.pickcar.auth.domain",
        "com.pickcar.company.domain",
        "com.pickcar.drivehistory.domain",
        "com.pickcar.rental.domain",
        "com.pickcar.reservation.domain",
        "com.pickcar.vehicle.domain",
        "com.pickcar.emulator.domain"
})
@ComponentScan(basePackages = {"com.pickcar", "com.pickcar.security"})
@EnableJpaAuditing
//@EnableMethodSecurity
@SpringBootApplication
public class DomainApplication {
    public static void main(String[] args) {
        SpringApplication.run(DomainApplication.class, args);
    }
}
