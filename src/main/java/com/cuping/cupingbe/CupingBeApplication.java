package com.cuping.cupingbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CupingBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CupingBeApplication.class, args);
    }

}
